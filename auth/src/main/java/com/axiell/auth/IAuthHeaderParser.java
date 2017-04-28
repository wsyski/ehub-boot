package com.axiell.auth;

public interface IAuthHeaderParser {
    public static final String BEARER_SCHEME = "Bearer";
    public static final String EHUB_SCHEME = "eHUB";

    AuthInfo parse(String value);

    String serialize(AuthInfo authInfo);
}
