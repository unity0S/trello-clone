package io.molnarsandor.trelloclone.security;

public class SecurityConstants {

    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String ACTIVATION_URL = "/api/users/activation/**";
    public static final String SECRET = "SecretKeyToGenerateJWTsForMyApp";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 3_600_000;
}
