package com.nanal.backend.domain.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetUserDto {
    @Schema(description = "닉네임" , example = "nanal123")
    private String userNickname;

    @Schema(description = "이메일" , example = "nanal123@gmail.com")
    private String userEmail;

    @Schema(description = "회고일" , example = "TUESDAY")
    private DayOfWeek userRetrospectDay;

}
