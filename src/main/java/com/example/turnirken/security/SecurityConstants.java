package com.example.turnirken.security;

class SecurityConstants {
    static final String SECRET = "SecretKeyToGenJWTs";
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/api/user/registration",
            "/api/user/registration/testLogin",
            "/api/user/registration/testEmail",
            "/api/login"
            // other public endpoints of your API may be appended to this array
    };

    static final String[] MODERATOR_LIST = {
            "/api/games/moderator/createOnDisplayGame",
            "/api/games/moderator/setOnDisplayGame",
    };

    static final String[] ADMIN_LIST = {
            "/api/games/moderator/createOnDisplayGame",
            "/api/games/moderator/setOnDisplayGame",
            "/api/user/setRole",
    };
}
