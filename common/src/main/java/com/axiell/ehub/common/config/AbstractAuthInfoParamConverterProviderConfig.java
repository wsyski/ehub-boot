package com.axiell.ehub.common.config;

import com.axiell.authinfo.IAuthHeaderParser;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.jwt.JwtAuthHeaderParser;
import com.axiell.ehub.common.controller.provider.converter.AuthInfoConverter;
import com.axiell.ehub.common.controller.provider.converter.AuthInfoParamConverterProvider;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAuthInfoParamConverterProviderConfig {

    protected AuthInfoParamConverterProvider authInfoParamConverterProvider(final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        JwtAuthHeaderParser jwtAuthHeaderParser = new JwtAuthHeaderParser(authHeaderSecretKeyResolver);
        Map<String, IAuthHeaderParser> authHeaderParsers = new HashMap<>();
        authHeaderParsers.put(JwtAuthHeaderParser.BEARER_SCHEME, jwtAuthHeaderParser);
        AuthInfoConverter authInfoConverter = new AuthInfoConverter(authHeaderParsers, IAuthHeaderParser.BEARER_SCHEME);
        return new AuthInfoParamConverterProvider(authInfoConverter);
    }
}
