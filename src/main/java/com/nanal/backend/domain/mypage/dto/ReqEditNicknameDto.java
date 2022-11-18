package com.nanal.backend.domain.mypage.dto;

import com.nanal.backend.domain.diary.dto.RespGetDiaryDto;
import com.nanal.backend.domain.mypage.service.MypageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ReqEditNicknameDto {
    private String nickname;
}
