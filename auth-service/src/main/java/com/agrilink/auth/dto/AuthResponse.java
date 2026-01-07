package com.agrilink.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO for authentication responses containing JWT token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type;
    private String email;
    private Set<String> roles;
    private long expiresIn;

    public static AuthResponse of(String token, String email, Set<String> roles, long expiresIn) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(email)
                .roles(roles)
                .expiresIn(expiresIn)
                .build();
    }
}
