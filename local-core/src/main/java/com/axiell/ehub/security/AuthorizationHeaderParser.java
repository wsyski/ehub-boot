package com.axiell.ehub.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationHeaderParser {
    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("\\s*(\\w*)\\s+(.*)");
    private static final int FIRST_GROUP = 1;
    private static final int SECOND_GROUP = 2;
    private AuthorizationHeaderParts authorizationHeaderParts;

    public AuthorizationHeaderParser(final String value) {
        if (value != null) {
            Matcher matcher = AUTHORIZATION_PATTERN.matcher(value);
            if (matcher.matches()) {
                String scheme = matcher.group(FIRST_GROUP);
                String parametersQuoted = matcher.group(SECOND_GROUP);
                int len=parametersQuoted.length();
                String parameters;
                if (len>1 && parametersQuoted.charAt(0)=='\"' && parametersQuoted.charAt(len-1)=='\"') {
                   parameters=parametersQuoted.substring(1,len-1);
                }
                else {
                    parameters=parametersQuoted;
                }
                authorizationHeaderParts = new AuthorizationHeaderParts(scheme, parameters);
            }
        }
    }

    public AuthorizationHeaderParts getAuthorizationHeaderParts() {
        return authorizationHeaderParts;
    }
}