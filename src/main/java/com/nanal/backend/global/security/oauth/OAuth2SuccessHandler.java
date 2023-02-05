package com.nanal.backend.global.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanal.backend.domain.auth.dto.LoginInfo;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.security.jwt.Token;
import com.nanal.backend.global.security.jwt.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final TokenUtil tokenUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Member member = Member.valueOf(oAuth2User);

        // 회원가입(가입 정보 없는 유저일 때만) 및 로그인
        Member loginMember = auth(member);

        // 토큰 생성
        Token token = tokenUtil.generateToken(loginMember);
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(loginMember.getSocialId(), token);

        // Response message 생성
        writeOauthResponse(response, new LoginInfo(loginMember.getNickname(), token));
    }

    private void writeOauthResponse(HttpServletResponse response, LoginInfo loginInfo)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        // Httpbody에 json 형태로 로그인 내용 추가
        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(loginInfo));
        writer.flush();
    }

    private Member auth(Member member) {
        Optional<Member> findMember = memberRepository.findBySocialId(member.getSocialId());
        if(newSubscribe(findMember)) return register(member);
        else return login(findMember);
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

