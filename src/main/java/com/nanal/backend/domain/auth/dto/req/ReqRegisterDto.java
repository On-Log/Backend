package com.nanal.backend.domain.auth.dto.req;

import com.nanal.backend.global.security.AuthenticationUtil;
import lombok.Data;

@Data
public class ReqRegisterDto {

    private String nickname;

    private String email;

    private String password;

    private String gender;

    private String ageRange;

    private String emailConfirmValue;

    public void encodePassword() {
        this.password = AuthenticationUtil.passwordEncoder.encode(password);
        System.out.println(password);
    }
}
