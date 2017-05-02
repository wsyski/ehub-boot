package com.axiell.authinfo;

public interface IAuthInfoResolver {

    AuthInfo resolve(String authorizationHeader);

    String serialize(AuthInfo authInfo);
}
