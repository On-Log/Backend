package com.nanal.backend.domain.mypage.dto;

import com.nanal.backend.domain.diary.dto.RespGetDiaryDto;
import com.nanal.backend.domain.mypage.service.MypageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ReqEditNicknameDto { //일단 disable - Enable하고 controller 단에 있던것 삭제.
    private String nickname;
    //private String email; - email 전송하면 안될듯?
}
