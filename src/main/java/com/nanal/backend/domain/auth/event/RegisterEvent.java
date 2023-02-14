package com.nanal.backend.domain.auth.event;

import com.nanal.backend.global.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RegisterEvent implements DomainEvent {

    private String nickname;
    private String email;
}
