package com.nanal.backend.domain.auth.dto.req;

import lombok.Data;

@Data
public class ReqLoginDto {

    private String email;

    private String password;
}
