package com.axiell.auth;

public interface IAuthInfoResolver {

    AuthInfo resolve(String authorizationHeader);

    String serialize(AuthInfo authInfo);
}
