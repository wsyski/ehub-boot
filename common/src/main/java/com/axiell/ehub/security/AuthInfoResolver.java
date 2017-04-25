package com.axiell.ehub.security;

import com.axiell.auth.*;
import com.axiell.ehub.ErrorCause;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

class AuthInfoResolver implements IAuthInfoResolver {
    private Map<String, IAuthHeaderParser> authHeaderParsers;
    private String defaultScheme;

    @Override
    public AuthInfo resolve(final String authorizationHeader) {
        final AuthorizationHeaderParser authorizationHeaderParser = new AuthorizationHeaderParser(authorizationHeader);
        final AuthorizationHeaderParts authorizationHeaderParts = authorizationHeaderParser.getAuthorizationHeaderParts();
        if (authorizationHeaderParts != null) {
            if (authorizationHeaderParts.getScheme() == null) {
                throw new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER);
            }
            IAuthHeaderParser authHeaderParser = authHeaderParsers.get(authorizationHeaderParts.getScheme());
            if (authHeaderParser == null) {
                throw new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER);
            }
            return authHeaderParser.parse(authorizationHeaderParts.getParameters());
        } else {
            throw new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER);
        }
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
