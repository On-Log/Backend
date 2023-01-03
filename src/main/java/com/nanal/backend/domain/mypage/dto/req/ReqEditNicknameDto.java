package com.nanal.backend.domain.mypage.dto.req;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class ReqEditNicknameDto {
    @NotBlank(message = "nickname 은 비어있을 수 없습니다.")
    @Size(max = 20, message="nickname 은 최대 20개의 문자만 입력 가능합니다.")
    private String nickname;
}
