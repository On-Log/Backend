package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.alarm.entity.Alarm;
import com.nanal.backend.domain.auth.dto.LoginInfo;
import com.nanal.backend.domain.auth.dto.req.ReqRegisterDto;
import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.domain.auth.event.RegisterEvent;
import com.nanal.backend.domain.auth.exception.*;
import com.nanal.backend.domain.auth.feign.AppleFeignClient;
import com.nanal.backend.domain.auth.feign.resp.Keys;
import com.nanal.backend.domain.auth.feign.resp.Keys.PubKey;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import com.nanal.backend.global.lock.DistributedLock;
import com.nanal.backend.global.lock.LockName;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.security.jwt.Token;
import com.nanal.backend.global.security.jwt.TokenUtil;
import io.jsonwebtoken.*;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Optional;

import static org.springframework.security.oauth2.jwt.JoseHeaderNames.KID;

@Timed("auth.api")
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenUtil tokenUtil;
    private final ClientNaver clientNaver;
    private final ClientKakao clientKakao;
    private final ClientGoogle clientGoogle;
    private final AppleFeignClient appleFeignClient;
    private final InternalAuthService internalAuthService;

    @Value("${app-id.apple}")
    private String apple_aud;

    @Counted("auth.api.count")
    public LoginInfo commonAuth(String accessToken, String providerInfo) {
        // 플랫폼에서 사용자 정보 조회
        Member member = getUserDataFromPlatform(accessToken, providerInfo);

        // 회원가입(가입 정보 없는 유저일 때만) 및 로그인
        Member authenticatedMember = internalAuthService.auth(member, providerInfo);

        AuthenticationUtil.makeAuthentication(authenticatedMember);

        // 토큰 생성
        Token token = tokenUtil.generateToken(authenticatedMember);
        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(authenticatedMember.getSocialId(), token);

        return new LoginInfo(authenticatedMember.getNickname(), token, authenticatedMember.getRole().equals(Member.Role.USER));
    }

    @Counted("auth.api.count")
    public LoginInfo appleAuth(String identityToken, String providerInfo) {
        // 토큰 서명 검증
        Jws<Claims> oidcTokenJws = sigVerificationAndGetJws(identityToken);
        // 토큰 바디 파싱해서 사용자 정보 획득
        String socialId = oidcTokenJws.getBody().getSubject();
        String email = (String) oidcTokenJws.getBody().get("email");
        Member member = Member.createAppleMember(socialId, email);

        // 회원가입(가입 정보 없는 유저일 때만) 및 로그인
        Member authenticatedMember = internalAuthService.auth(member, providerInfo);

        AuthenticationUtil.makeAuthentication(authenticatedMember);

        // 토큰 생성
        Token token = tokenUtil.generateToken(authenticatedMember);
        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(authenticatedMember.getSocialId(), token);

        return new LoginInfo(authenticatedMember.getNickname(), token, authenticatedMember.getRole().equals(Member.Role.USER));
    }

    @Counted("auth.api.count")
    public Token reissue(String token) {
        // refresh 토큰이 유효한지 확인
        if (token != null && tokenUtil.verifyToken(token)) {

            // 토큰 새로 받아오기
            Token reissueToken = tokenUtil.tokenReissue(token);

            return reissueToken;
        }

        throw RefreshTokenInvalidException.EXCEPTION;
    }

    //===편의 메서드===//
    private Member getUserDataFromPlatform(String accessToken, String providerInfo) {
        if(providerInfo.contains("google")) return clientGoogle.getUserData(accessToken);
        else if(providerInfo.contains("kakao")) return clientKakao.getUserData(accessToken);
        else return clientNaver.getUserData(accessToken);
    }

    private Jws<Claims> sigVerificationAndGetJws(String unverifiedToken) {

        String kid = getKidFromUnsignedTokenHeader(
                unverifiedToken,
                "https://appleid.apple.com",
                apple_aud);

        Keys keys = appleFeignClient.getKeys();
        PubKey pubKey = keys.getKeys().stream()
                .filter((key) -> key.getKid().equals(kid))
                .findAny()
                .get();

        return getOIDCTokenJws(unverifiedToken, pubKey.getN(), pubKey.getE());
    }

    public Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getRSAPublicKey(modulus, exponent))
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            log.error(e.toString());
            throw TokenInvalidException.EXCEPTION;
        }
    }

    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }

    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud) {
        return (String) getUnsignedTokenClaims(token, iss, aud).getHeader().get(KID);
    }

    private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud) {
        try {
            return Jwts.parserBuilder()
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .build()
                    .parseClaimsJwt(getUnsignedToken(token));
        } catch (Exception e) {
            log.error(e.toString());
            throw TokenInvalidException.EXCEPTION;
        }
    }

    private String getUnsignedToken(String token) {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) throw TokenInvalidException.EXCEPTION;
        return splitToken[0] + "." + splitToken[1] + ".";
    }
}
