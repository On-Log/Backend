package com.nanal.backend.global.security.jwt;

import com.nanal.backend.domain.auth.exception.RefreshTokenInvalidException;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenUtil {

    // Access : 3분
    @Value("${jwt.token.access-period}")
    private long accessPeriod;
    // Refresh : 10분
    @Value("${jwt.token.refresh-period}")
    private long refreshPeriod;

    @Value("${jwt.token.reissue-period}")
    private long reissuePeriod;
    @Value("${jwt.token.refresh-token-storage-period}")
    private long refreshTokenStoragePeriod;
    @Value("${jwt.token.secret}")
    private String secretKey;
    
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }

    public Token generateToken(Member member) {
        // claim 생성
        Claims claims = getClaims(member);

        // Access, Refresh 토큰 생성 후 반환
        Date now = new Date();

        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + accessPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact());
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Token tokenReissue(String token) {
        String socialId = getSocialId(token);
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(()-> RefreshTokenInvalidException.EXCEPTION);
        // socialId 에 해당하는 refreshToken redis 에서 가져오기
        String storedRefreshToken = redisTemplate.opsForValue().get(socialId);
        // socialId 에 해당하는 refreshToken 이 없거나 일치하지 않을 때
        if(storedRefreshToken == null || !storedRefreshToken.equals(token)) throw RefreshTokenInvalidException.EXCEPTION;

        // Token 생성
        Token newToken = generateToken(member);

        Date expireDate = getExpiration(token);
        Date currentDate = new Date();
        // refreshToken 기간이 얼마남지 않았을 경우 (3일 미만)
        log.info("remain time = {} < {}", expireDate.getTime() - currentDate.getTime(), reissuePeriod);
        if (expireDate.getTime() - currentDate.getTime() < reissuePeriod) storeRefreshToken(socialId, newToken);
        // refreshToken 의 유효기간이 3일 이상 남았을 경우 (refreshToken NULL 값으로 설정함으로써 전송하지 않음)
        else newToken.setRefreshToken(null);

        return newToken;
    }

    private Date getExpiration(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }

    public String getSocialId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getEmail(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email");
    }

    public String getRole(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role");
    }

    public void storeRefreshToken(String socialId, Token token) {
        redisTemplate.opsForValue().set(
                socialId,
                token.getRefreshToken(),
                refreshTokenStoragePeriod,
                TimeUnit.SECONDS
        );
    }

    private static Claims getClaims(Member member) {
        // claim 에 socialId 정보 추가
        Claims claims = Jwts.claims().setSubject(member.getSocialId());
        // claim 에 email 정보 추가
        //claims.put("email", member.getEmail());
        // claim 에 권한 정보 추가
        //claims.put("role", member.getRole().getKey());
        return claims;
    }

    public void expireRefreshToken(String socialId) {
        redisTemplate.delete(socialId);
    }
}
