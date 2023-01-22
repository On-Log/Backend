package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.GoogleUserResponseDto;
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
public class ClientGoogle {

    private final WebClient webClient;

    public Member getUserData(String accessToken) {
        GoogleUserResponseDto googleUserResponseDto = webClient.get()
                .uri("https://oauth2.googleapis.com/tokeninfo", builder -> builder.queryParam("id_token", accessToken).build())
                // KAKAO와 달리 GOOGLE을 IdToken을 query parameter로 받습니다. 이로 인해 KAKAO와 uri 작성 방식이 상이합니다.
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RuntimeException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleUserResponseDto.class)
                .block();

        googleUserResponseDto.adaptResponse();

        return Member.builder()
                .socialId(MemberProvider.GOOGLE + "@" + googleUserResponseDto.getSub())
                .provider(MemberProvider.GOOGLE)
                .name(googleUserResponseDto.getName())
                .email(googleUserResponseDto.getEmail())
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .resetAvail(Boolean.TRUE)
                .nickname(googleUserResponseDto.getName())
                .ageRange("undef")
                .gender("undef")
                .role(Member.Role.USER)
                .build();
    }
}
