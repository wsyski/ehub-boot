package com.axiell.auth;

import com.axiell.auth.util.AuthRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class AuthInfoResolver implements IAuthInfoResolver {
    private Map<String, IAuthHeaderParser> authHeaderParsers;
    private String defaultScheme;

    @Override
    public AuthInfo resolve(final String authorizationHeader) {
        final AuthorizationHeaderParser authorizationHeaderParser = new AuthorizationHeaderParser(authorizationHeader);
        final AuthorizationHeaderParts authorizationHeaderParts = authorizationHeaderParser.getAuthorizationHeaderParts();
        if (authorizationHeaderParts != null) {
            String scheme = authorizationHeaderParts.getScheme() == null ? StringUtils.EMPTY : authorizationHeaderParts.getScheme();
            IAuthHeaderParser authHeaderParser = authHeaderParsers.get(scheme);
            if (authHeaderParser != null) {
                return authHeaderParser.parse(authorizationHeaderParts.getParameters());
            }
        }
        throw new AuthRuntimeException("Missing or Invalid Authorization Header");
    }

    @Override
    public String serialize(final AuthInfo authInfo) {
        IAuthHeaderParser authHeaderParser = authHeaderParsers.get(defaultScheme);
        return authHeaderParser.serialize(authInfo);
    }

    @Required
    public void setAuthHeaderParsers(final Map<String, IAuthHeaderParser> authHeaderParsers) {
        this.authHeaderParsers = authHeaderParsers;
    }

    @Required
    public void setDefaultScheme(final String defaultScheme) {
        this.defaultScheme = defaultScheme;
    }
}
