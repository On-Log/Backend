package com.nanal.backend.domain.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ReqSignUpDto {

    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    @Size(max = 50, message="이메일의 길이는 최대 50자 입니다.")
    private String email;

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    @Size(max = 20, message="이름의 길이는 최대 20자 입니다.")
    private String name;

    @NotBlank(message = "provider 는 비어있을 수 없습니다.")
    @Size(max = 10, message="provider 길이는 최대 10자 입니다.")
    private String provider;
}
