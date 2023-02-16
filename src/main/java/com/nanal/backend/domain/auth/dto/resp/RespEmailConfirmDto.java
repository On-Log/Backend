package com.nanal.backend.domain.auth.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RespEmailConfirmDto {
    private String emailConfirmValue;
}
