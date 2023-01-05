package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.KakaoUserResponseDto;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ClientKakao{

    private final WebClient webClient;

    // TODO ADMIN 유저 생성 시 getAdminUserData 메소드 생성 필요
    public Member getUserData(String accessToken) {
        KakaoUserResponseDto kakaoUserResponseDto = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me") // KAKAO의 유저 정보 받아오는 url
                .headers(h -> h.setBearerAuth(accessToken)) // JWT 토큰을 Bearer 토큰으로 지정
                .retrieve()
                // 아래의 onStatus는 error handling
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RuntimeException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserResponseDto.class) // KAKAO의 유저 정보를 넣을 Dto 클래스
                .block();

        return Member.builder()
                .socialId(MemberProvider.KAKAO + "@" + kakaoUserResponseDto.getId())
                .provider(MemberProvider.KAKAO)
                .name(kakaoUserResponseDto.getProperties().getNickname())
                .email(kakaoUserResponseDto.getKakaoAccount().getEmail())
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .resetAvail(Boolean.TRUE)
                .nickname("Anonymous User")
                .role(Member.Role.USER)
                .build();
    }
}