package com.nanal.backend.domain.mypage.dto;

import com.nanal.backend.domain.diary.dto.RespGetDiaryDto;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqGetUserDto {

    private String userNickName;
    private String userEmail;
    private DayOfWeek retrospectDay;
}
