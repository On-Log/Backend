package com.nanal.backend.global.security;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String socialId;
    private String email;
}
