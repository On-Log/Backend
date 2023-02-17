package com.nanal.backend.global.security;

import com.nanal.backend.domain.auth.entity.Member;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String socialId;
    private String email;
    private String role;
}
