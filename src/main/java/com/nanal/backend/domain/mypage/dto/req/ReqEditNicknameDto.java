package com.nanal.backend.domain.mypage.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReqEditNicknameDto {
    @NotBlank(message = "nickname 은 비어있을 수 없습니다.")
    @Size(min = 1, max = 7, message="nickname 은 최소 1개, 최대 7개의 문자만 입력 가능합니다.")
    private String nickname;
}
