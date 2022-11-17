package com.nanal.backend.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespEditNicknameDto {
    // 마이페이지에 필요한 닉네임, 이메일, 회고일
    private String userNickname;

}
