package com.axiell.authinfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.ext.ParamConverter;
import java.util.Map;

public class AuthInfoConverter implements ParamConverter<AuthInfo> {
    private Map<String, IAuthHeaderParser> authHeaderParsers;
    private String defaultScheme;

    @Override
    public AuthInfo fromString(final String authorizationHeader) {
        final AuthorizationHeaderParser authorizationHeaderParser = new AuthorizationHeaderParser(authorizationHeader);
        final AuthorizationHeaderParts authorizationHeaderParts = authorizationHeaderParser.getAuthorizationHeaderParts();
        if (authorizationHeaderParts != null) {
            String scheme = authorizationHeaderParts.getScheme() == null ? StringUtils.EMPTY : authorizationHeaderParts.getScheme();
            IAuthHeaderParser authHeaderParser = authHeaderParsers.get(scheme);
            if (authHeaderParser != null) {
                return authHeaderParser.parse(authorizationHeaderParts.getParameters());
            }
        }
        throw new MissingOrUnparseableAuthorizationHeaderRuntimeException();
    }

    @Override
    public String toString(final AuthInfo authInfo) {
        IAuthHeaderParser authHeaderParser = authHeaderParsers.get(defaultScheme);
        return defaultScheme + " " + authHeaderParser.serialize(authInfo);
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
