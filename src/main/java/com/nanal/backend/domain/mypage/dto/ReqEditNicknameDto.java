package com.nanal.backend.domain.mypage.dto;

import com.nanal.backend.domain.diary.dto.RespGetDiaryDto;
import com.nanal.backend.domain.mypage.service.MypageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class ReqEditNicknameDto {
    @NotNull(message = "닉네임은 비어있을 수 없습니다.")
    @Max(value = 20, message="최대 20개의 문자만 입력 가능합니다.")
    private String nickname;
}
