package com.axiell.auth;

public interface IAuthHeaderParser {
    static final String BEARER_SCHEME = "Bearer";
    static final String EHUB_SCHEME = "eHUB";

    AuthInfo parse(String value);

    String serialize(AuthInfo authInfo);
}
