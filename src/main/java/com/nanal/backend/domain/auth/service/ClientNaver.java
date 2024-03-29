package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.NaverUserResponseDto;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.global.exception.customexception.InternalServerErrorException;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ClientNaver {
    private final WebClient webClient;

    public Member getUserData(String accessToken) {
        NaverUserResponseDto naverUserResponseDto = webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me") // Naver의 유저 정보 받아오는 url
                .headers(h -> h.setBearerAuth(accessToken)) // JWT 토큰을 Bearer 토큰으로 지정
                .retrieve()
                // 아래의 onStatus는 error handling
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(TokenInvalidException.EXCEPTION))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new InternalServerErrorException("Naver Internal Server Error ")))
                .bodyToMono(NaverUserResponseDto.class) // Naver의 유저 정보를 넣을 Dto 클래스
                .block();

        naverUserResponseDto.adaptResponse();

        return Member.builder()
                .socialId(MemberProvider.NAVER + "@" + naverUserResponseDto.getResponse().getId())
                .provider(MemberProvider.NAVER)
                .name(naverUserResponseDto.getResponse().getNickname())
                .email(naverUserResponseDto.getResponse().getEmail())
                .password("undef")
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .prevRetrospectDate(LocalDateTime.now().minusDays(30))
                .nickname(naverUserResponseDto.getResponse().getNickname())
                .gender(naverUserResponseDto.getResponse().getGender())
                .ageRange(naverUserResponseDto.getResponse().getAge())
                .role(Member.Role.ONBOARDER)
                .build();
    }
}
