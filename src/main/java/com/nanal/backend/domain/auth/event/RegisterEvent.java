package com.nanal.backend.domain.auth.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RegisterEvent {

    private String nickname;
    private String email;
}
