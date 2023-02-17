package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.LoginInfo;
import com.nanal.backend.domain.auth.dto.req.ReqRegisterDto;
import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.domain.auth.event.RegisterEvent;
import com.nanal.backend.domain.auth.exception.*;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.security.jwt.Token;
import com.nanal.backend.global.security.jwt.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final ApplicationEventPublisher publisher;
    private final RedisTemplate<String, String> redisTemplate;

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

    public LoginInfo commonAuth(String accessToken, String providerInfo) {
        // 플랫폼에서 사용자 정보 조회
        Member member = getUserDataFromPlatform(accessToken, providerInfo);

        // 회원가입(가입 정보 없는 유저일 때만) 및 로그인
        Member authenticatedMember = auth(member);

        // 토큰 생성
        Token token = tokenUtil.generateToken(authenticatedMember);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(authenticatedMember.getSocialId(), token);

        return new LoginInfo(authenticatedMember.getNickname(), token, authenticatedMember.getRole().equals(Member.Role.ONBOARDER));
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

    private Member auth(Member member) {
        Optional<Member> findMember = memberRepository.findBySocialId(member.getSocialId());
        if(isNewMember(findMember))
            return joinMembership(member);
        else
            return login(findMember);
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

    private Member login(Optional<Member> member) {
        return member.get();
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
}
