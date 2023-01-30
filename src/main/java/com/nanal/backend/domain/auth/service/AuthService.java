package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.req.ReqAuthDto;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.jwt.Token;
import com.nanal.backend.global.security.jwt.TokenUtil;
import com.nanal.backend.domain.auth.exception.RefreshTokenInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    public Token naverAuth(ReqAuthDto reqAuthDto) {
        Member naverMember = clientNaver.getUserData(reqAuthDto.getAccessToken());

        // 회원가입
        register(naverMember);

        // 토큰 생성
        Token token = tokenUtil.generateToken(naverMember);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(naverMember.getSocialId(), token);

        return token;
    }

    public Token kakaoAuth(ReqAuthDto reqAuthDto) {
        Member kakaoMember = clientKakao.getUserData(reqAuthDto.getAccessToken());

        // 회원가입
        register(kakaoMember);

        // 토큰 생성
        Token token = tokenUtil.generateToken(kakaoMember);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(kakaoMember.getSocialId(), token);

        return token;
    }

    public Token googleAuth(ReqAuthDto reqAuthDto) {
        Member googleMember = clientGoogle.getUserData(reqAuthDto.getAccessToken());

        // 회원가입
        register(googleMember);

        // 토큰 생성
        Token token = tokenUtil.generateToken(googleMember);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(googleMember.getSocialId(), token);

        return token;
    }

    public Token reissue(String token) {
        // refresh 토큰이 유효한지 확인
        if (token != null && tokenUtil.verifyToken(token)) {

            // 토큰 새로 받아오기
            Token reissueToken = tokenUtil.tokenReissue(token);

            return reissueToken;
        }

        throw new RefreshTokenInvalidException(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    //===편의 메서드===//

    private void register(Member member) {
        if(memberRepository.findBySocialId(member.getSocialId()).isEmpty()) memberRepository.save(member);
    }
}
