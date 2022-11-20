package com.nanal.backend.domain.mypage.dto;

import java.time.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqGetUserDto {
    @Schema(description = "닉네임" , example = "nanal123")
    @NotBlank(message = "nickname 은 비어있을 수 없습니다.")
    @Size(max = 20, message="nickname 은 최대 20개의 문자만 입력 가능합니다.")
    private String userNickName;

    @Schema(description = "이메일" , example = "nanal123@gmail.com")
    @NotBlank(message = "email 은 비어있을 수 없습니다.")
    @Size(max = 50, message="email 은 최대 50개의 문자만 입력 가능합니다.")
    private String userEmail;

    @Schema(description = "회고일" , example = "TUESDAY")
    private DayOfWeek retrospectDay;
}
