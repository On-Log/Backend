package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.LoginInfo;
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
import java.util.Optional;

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

    public LoginInfo commonAuth(String accessToken, String providerInfo) {
        // 플랫폼에서 사용자 정보 조회
        Member member = getUserDataFromPlatform(accessToken, providerInfo);

        // 회원가입(가입 정보 없는 유저일 때만) 및 로그인
        Member loginMember = auth(member);

        // 토큰 생성
        Token token = tokenUtil.generateToken(loginMember);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(loginMember.getSocialId(), token);

        return new LoginInfo(loginMember.getNickname(), token);
    }

    private Member auth(Member member) {
        Optional<Member> findMember = memberRepository.findBySocialId(member.getSocialId());
        if(newSubscribe(findMember)) return register(member);
        else return login(findMember);
    }

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

    private static boolean newSubscribe(Optional<Member> findMember) {
        return findMember.isEmpty();
    }

    private Member register(Member member) {
        return memberRepository.save(member);
    }

    private Member login(Optional<Member> member) {
        return member.get();
    }
}
