package com.nanal.backend.domain.mypage.dto;

import com.nanal.backend.domain.diary.dto.RespGetDiaryDto;
import java.time.LocalDateTime;
import java.time.DayOfWeek;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqGetUserDto {
    @Schema(description = "닉네임" , example = "nanal123")
    @NotNull(message = "닉네임은 비어있을 수 없습니다.")
    @Size(max = 20, message="최대 20개의 문자만 입력 가능합니다.")
    private String userNickName;

    @Schema(description = "이메일" , example = "nanal123@gmail.com")
    @NotNull(message = "이메일은 비어있을 수 없습니다.")
    @Size(max = 50, message="최대 50개의 문자만 입력 가능합니다.")
    private String userEmail;

    @Schema(description = "회고일" , example = "TUESDAY")
    @NotNull(message = "회고일은 비어있을 수 없습니다.")
    private DayOfWeek retrospectDay;
}
