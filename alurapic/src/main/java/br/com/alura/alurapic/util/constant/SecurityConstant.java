package br.com.alura.alurapic.util.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 28_800_000;//8 hours in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "X-Access-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";

    public static final String ALURA = "Alura";
    public static final String ALURA_PIC = "Alura Pic";

    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
//    public static final String[] PUBLIC_URLS = {"/api/v1/user/register","/login",
//            "/api/v1/user/image/**", "/api/v1/user/reset-password/**"};
    public static final String[] PUBLIC_URLS = {"**"};
}
