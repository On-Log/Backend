package com.nanal.backend.domain.auth.service;

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
@Transactional
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenUtil tokenUtil;
    private final ClientNaver clientNaver;
    private final ClientKakao clientKakao;
    private final ClientGoogle clientGoogle;
    private final ApplicationEventPublisher publisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final AppleFeignClient appleFeignClient;

    @Value("${app-id.apple}")
    private String apple_aud;

    @Counted("auth.api.count")
    public void generalRegister(ReqRegisterDto reqRegisterDto) {
        // todo : 이메일 인증완료시 redis 에 저장해둔 이메일:인증값과 요청으로 들어온 값들이 일치하는지 확인후에 일치하면 값 삭제
        verifyEmailConfirmValue(reqRegisterDto.getEmail(), reqRegisterDto.getEmailConfirmValue());

        memberRepository.findByEmail(MemberProvider.GENERAL + "#" + reqRegisterDto.getEmail())
                .ifPresent(m -> { throw EmailAlreadyExistException.EXCEPTION; });

        reqRegisterDto.encodePassword();
        Member newMember = Member.createNewMember(reqRegisterDto);
        publisher.publishEvent(new RegisterEvent(newMember.getNickname(), newMember.getEmail()));

        memberRepository.save(newMember);
    }

    @Counted("auth.api.count")
    public LoginInfo generalLogin(ReqRegisterDto reqRegisterDto) {
        Member loginMember = memberRepository.findByEmail(MemberProvider.GENERAL + "#" + reqRegisterDto.getEmail())
                .orElseThrow(() -> AccountNotExistException.EXCEPTION);

        verifyPassword(reqRegisterDto.getPassword(), loginMember.getPassword());

        // 토큰 생성
        Token token = tokenUtil.generateToken(loginMember);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(loginMember.getSocialId(), token);

        return new LoginInfo(loginMember.getNickname(), token);
    }

    @Counted("auth.api.count")
    public LoginInfo commonAuth(String accessToken, String providerInfo) {
        // 플랫폼에서 사용자 정보 조회
        Member member = getUserDataFromPlatform(accessToken, providerInfo);

        // 회원가입(가입 정보 없는 유저일 때만) 및 로그인
        Member authenticatedMember = auth(member, providerInfo);

        AuthenticationUtil.makeAuthentication(authenticatedMember);

        // 토큰 생성
        Token token = tokenUtil.generateToken(authenticatedMember);
        // todo: 테스트를 위한 로깅
        log.info("Token : {}", token.getToken());
        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(authenticatedMember.getSocialId(), token);

        return new LoginInfo(authenticatedMember.getNickname(), token, authenticatedMember.getRole().equals(Member.Role.ONBOARDER));
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
        Member authenticatedMember = auth(member, providerInfo);

        AuthenticationUtil.makeAuthentication(authenticatedMember);

        // 토큰 생성
        Token token = tokenUtil.generateToken(authenticatedMember);
        // todo: 테스트를 위한 로깅
        log.info("Token : {}", token.getToken());
        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(authenticatedMember.getSocialId(), token);

        return new LoginInfo(authenticatedMember.getNickname(), token, authenticatedMember.getRole().equals(Member.Role.ONBOARDER));
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

    private synchronized Member auth(Member member, String providerInfo) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(isNewMember(findMember))
            return joinMembership(member);
        else
            return login(findMember, providerInfo);
    }

    private Member joinMembership(Member member) {
        Member newMember = register(member);
        publisher.publishEvent(new RegisterEvent(newMember.getNickname(), newMember.getEmail()));
        return newMember;
    }

    private static boolean isNewMember(Optional<Member> findMember) {
        return findMember.isEmpty();
    }

    private Member register(Member member) {
        return memberRepository.save(member);
    }

    private Member login(Optional<Member> member, String providerInfo) {
        Member loginMember = member.get();
        if(!providerInfo.contains((loginMember.getProvider().name().toLowerCase())))
            throw AccountAlreadyExistException.EXCEPTION;
        return loginMember;
    }

    private void verifyPassword(String rawPassword, String encodedPassword) {
        if(isIncorrectPassword(rawPassword, encodedPassword)) throw PasswordIncorrectException.EXCEPTION;
    }

    private boolean isIncorrectPassword(String rawPassword, String encodedPassword) {
        return !AuthenticationUtil.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void verifyEmailConfirmValue(String email, String emailConfirmValue) {
        if(isIncorrectEmailConfirmValue(email, emailConfirmValue)) throw InvalidConfirmValueException.EXCEPTION;
        redisTemplate.delete(email);
    }

    private boolean isIncorrectEmailConfirmValue(String email, String emailConfirmValue) {
        return !emailConfirmValue.equals(redisTemplate.opsForValue().get(email));
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
