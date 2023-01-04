package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.req.ReqAuthDto;
import com.nanal.backend.domain.auth.dto.req.ReqSignUpDto;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.security.jwt.Token;
import com.nanal.backend.global.security.jwt.TokenUtil;
import com.nanal.backend.domain.auth.exception.RefreshTokenInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenUtil tokenUtil;
    private final ClientKakao clientKakao;
    private final ClientGoogle clientGoogle;

    public Token kakaoAuth(ReqAuthDto reqAuthDto) {
        Member kakaoMember = clientKakao.getUserData(reqAuthDto.getAccessToken());
        String socialId = kakaoMember.getSocialId();

        // 최초 로그인이라면 회원가입 처리를 한다.
        if(memberRepository.findBySocialId(socialId).isEmpty()) memberRepository.save(kakaoMember);

        // socialId로 Authentication 정보 생성
        AuthenticationUtil.makeAuthentication(socialId, kakaoMember.getEmail());

        // 토큰 생성
        Token token = tokenUtil.generateToken(socialId);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(socialId, token);

        log.info("Redis 저장 완료");

        return token;
    }

    public Token googleAuth(ReqAuthDto reqAuthDto) {
        Member googleMember = clientGoogle.getUserData(reqAuthDto.getAccessToken());
        String socialId = googleMember.getSocialId();

        // 최초 로그인이라면 회원가입 처리를 한다.
        if(memberRepository.findBySocialId(socialId).isEmpty()) memberRepository.save(googleMember);

        // socialId로 Authentication 정보 생성
        AuthenticationUtil.makeAuthentication(socialId, googleMember.getEmail());

        // 토큰 생성
        Token token = tokenUtil.generateToken(socialId);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(socialId, token);

        log.info("Redis 저장 완료");

        return token;
    }

    public Token reissue(String token) {
        // refresh 토큰이 유효한지 확인
        if (token != null && tokenUtil.verifyToken(token)) {
            String socialId = tokenUtil.getUid(token);
            // 토큰 새로 받아오기
            Token reissueToken = tokenUtil.tokenReissue(token);

            return reissueToken;
        }

        throw new RefreshTokenInvalidException("Refresh Token 이 유효하지 않습니다.");
    }


}
