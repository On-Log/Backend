package com.nanal.backend.global.security;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String socialId;
    private String email;
    private List<String> roles;
}
