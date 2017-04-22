package com.axiell.ehub.security;

public interface IAuthInfoResolver {

    AuthInfo resolve(String authorizationHeader);

    String serialize(AuthInfo authInfo);
}
