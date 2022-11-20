package com.nanal.backend.domain.auth.dto;

import lombok.Data;

@Data
public class ReqSignUpDto {

    private String email;

    private String name;

    private String provider;
}
