package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.alarm.entity.Alarm;
import com.nanal.backend.domain.alarm.repository.AlarmRepository;
import com.nanal.backend.domain.auth.dto.KakaoAccessTokenResponseDto;
import com.nanal.backend.domain.auth.dto.KakaoUserResponseDto;
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

@Component
@RequiredArgsConstructor
public class ClientKakao{

    @Value("${app-id.kakao}")
    private String appId;

    private final WebClient webClient;

    public Member getUserData(String accessToken) {
        // Access Token 검증
        verifyAccessToken(accessToken);

        KakaoUserResponseDto kakaoUserResponseDto = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me") // KAKAO의 유저 정보 받아오는 url
                .headers(h -> h.setBearerAuth(accessToken)) // JWT 토큰을 Bearer 토큰으로 지정
                .retrieve()
                // 아래의 onStatus는 error handling
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(TokenInvalidException.EXCEPTION))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new InternalServerErrorException("Kakao Internal Server Error ")))
                .bodyToMono(KakaoUserResponseDto.class) // KAKAO의 유저 정보를 넣을 Dto 클래스
                .block();

        kakaoUserResponseDto.adaptResponse();

        return Member.createKakaoMember(kakaoUserResponseDto);
    }

    public void verifyAccessToken(String accessToken) {
        KakaoAccessTokenResponseDto kakaoAccessTokenResponseDto = webClient.get()
                .uri("https://kapi.kakao.com/v1/user/access_token_info") // Access Token 정보 요청
                .headers(h -> h.setBearerAuth(accessToken)) // JWT 토큰을 Bearer 토큰으로 지정
                .retrieve()
                // 아래의 onStatus는 error handling
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(TokenInvalidException.EXCEPTION))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new InternalServerErrorException("Kakao Internal Server Error ")))
                .bodyToMono(KakaoAccessTokenResponseDto.class) // Access Token 정보를 넣을 Dto 클래스
                .block();

        if(!kakaoAccessTokenResponseDto.getAppId().equals(appId)) throw TokenInvalidException.EXCEPTION;
    }
}