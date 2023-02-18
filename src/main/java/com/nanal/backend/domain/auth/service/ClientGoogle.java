package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.GoogleUserResponseDto;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.global.exception.customexception.InternalServerErrorException;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ClientGoogle {

    @Value("${app-id.google}")
    private String appId;

    private final WebClient webClient;

    public void verifyAccessToken(String aud) {
        String extractedAppId = Arrays.stream(aud.split("-"))
                .findFirst()
                .orElseThrow(() -> TokenInvalidException.EXCEPTION);

        if(!appId.equals(extractedAppId)) throw TokenInvalidException.EXCEPTION;
    }

    public Member getUserData(String accessToken) {
        GoogleUserResponseDto googleUserResponseDto = webClient.get()
                .uri("https://oauth2.googleapis.com/tokeninfo", builder -> builder.queryParam("id_token", accessToken).build())
                // KAKAO와 달리 GOOGLE을 IdToken을 query parameter로 받습니다. 이로 인해 KAKAO와 uri 작성 방식이 상이합니다.
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(TokenInvalidException.EXCEPTION))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new InternalServerErrorException("Google Internal Server Error ")))
                .bodyToMono(GoogleUserResponseDto.class)
                .block();

        verifyAccessToken(googleUserResponseDto.getAud());

        googleUserResponseDto.adaptResponse();

        return Member.builder()
                .socialId(MemberProvider.GOOGLE + "@" + googleUserResponseDto.getSub())
                .provider(MemberProvider.GOOGLE)
                .name(googleUserResponseDto.getName())
                .email(googleUserResponseDto.getEmail())
                .password("undef")
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .prevRetrospectDate(LocalDateTime.now().minusDays(30))
                .nickname(googleUserResponseDto.getName())
                .ageRange("undef")
                .gender("undef")
                .role(Member.Role.ONBOARDER)
                .build();
    }
}