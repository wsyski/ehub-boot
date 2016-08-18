package com.axiell.ehub.security;

interface IAuthInfoResolver {

    AuthInfo resolve(String authorizationHeader);
}
