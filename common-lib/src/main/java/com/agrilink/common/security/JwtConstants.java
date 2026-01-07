package com.agrilink.common.security;

/**
 * Constants for JWT token handling across all services.
 */
public final class JwtConstants {
    
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    
    private JwtConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}
