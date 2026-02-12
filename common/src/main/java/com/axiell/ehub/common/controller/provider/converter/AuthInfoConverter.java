package com.axiell.ehub.common.controller.provider.converter;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.AuthorizationHeaderParser;
import com.axiell.authinfo.AuthorizationHeaderParts;
import com.axiell.authinfo.IAuthHeaderParser;
import com.axiell.authinfo.MissingOrUnparseableAuthorizationHeaderRuntimeException;
import org.apache.commons.lang3.StringUtils;

import jakarta.ws.rs.ext.ParamConverter;
import java.util.Map;

public class AuthInfoConverter implements ParamConverter<AuthInfo> {
    private final Map<String, IAuthHeaderParser> authHeaderParsers;
    private final String defaultScheme;

    public AuthInfoConverter(Map<String, IAuthHeaderParser> authHeaderParsers, String defaultScheme) {
        this.authHeaderParsers = authHeaderParsers;
        this.defaultScheme = defaultScheme;
    }

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
}
