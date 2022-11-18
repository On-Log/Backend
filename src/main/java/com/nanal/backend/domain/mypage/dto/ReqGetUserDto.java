package com.nanal.backend.domain.mypage.dto;

import com.nanal.backend.domain.diary.dto.RespGetDiaryDto;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqGetUserDto {
    @NotNull(message = "닉네임은 비어있을 수 없습니다.")
    @Max(value = 20, message="최대 20개의 문자만 입력 가능합니다.")
    private String userNickName;

    @NotNull(message = "이메일은 비어있을 수 없습니다.")
    @Max(value = 50, message="최대 50개의 문자만 입력 가능합니다.")
    private String userEmail;

    @NotNull(message = "회고일은 비어있을 수 없습니다.")
    private DayOfWeek retrospectDay;
}
