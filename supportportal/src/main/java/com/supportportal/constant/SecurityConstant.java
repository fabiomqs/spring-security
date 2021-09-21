package com.supportportal.constant;

public class SecurityConstant {

    public static final long EXPIRATION_TIME = 432_000_000;//5 days in milliseconds
    //public static final long EXPIRATION_TIME = 28_800_000;//8 hours in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "JWT-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String GET_ARRAYS_LLC = "Get Arrays, LLC";
    public static final String GET_ARRAYS_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/login","/api/v1/user/register",
            "/api/v1/user/resetpassword/**", "/api/v1/user/image/**"};
//    public static final String[] PUBLIC_URLS = {"**"};
}