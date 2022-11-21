package com.nanal.backend.domain.auth;

import com.nanal.backend.domain.auth.dto.ReqSignUpDto;
import com.nanal.backend.domain.mypage.repository.MemberRepository;
import com.nanal.backend.entity.Member;
import com.nanal.backend.global.auth.AuthenticationUtil;
import com.nanal.backend.global.auth.UserDto;
import com.nanal.backend.global.auth.token.Token;
import com.nanal.backend.global.auth.token.TokenUtil;
import com.nanal.backend.global.exception.customexception.RefreshTokenInvalidException;
import com.nanal.backend.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenUtil tokenUtil;

    public Token signUp(ReqSignUpDto reqSignUpDto) {
        // 최초 로그인이라면 회원가입 처리를 한다.
        if (memberRepository.findByEmail(reqSignUpDto.getEmail()).isEmpty()) {
            Member newMember = Member.builder()
                    .provider(reqSignUpDto.getProvider())
                    .name(reqSignUpDto.getName())
                    .email(reqSignUpDto.getEmail())
                    // 당일로 회고일 설정
                    .retrospectDay(LocalDate.now().getDayOfWeek())
                    .resetAvail(Boolean.TRUE)
                    .nickname("Anonymous User")
                    .role(Member.Role.USER)
                    .build();
            memberRepository.save(newMember);


        }

        // 이메일로 Authentication 정보 생성
        AuthenticationUtil.makeAuthentication(reqSignUpDto.getEmail());

        // 토큰 생성
        Token token = tokenUtil.generateToken(reqSignUpDto.getEmail());
        log.info("{}", token);

        // Redis에 Refresh Token 저장
        tokenUtil.storeRefreshToken(reqSignUpDto.getEmail(), token);

        log.info("Redis 저장 완료");

        return token;
    }

    public Token reissue(String token) {
        // refresh 토큰이 유효한지 확인
        if (token != null && tokenUtil.verifyToken(token)) {
            String email = tokenUtil.getUid(token);
            // 토큰 새로 받아오기
            Token reissueToken = tokenUtil.tokenReissue(token);

            return reissueToken;
        }

        throw new RefreshTokenInvalidException("Refresh Token 이 유효하지 않습니다.");
    }
}
