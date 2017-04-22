package com.axiell.ehub.security;

public class AuthorizationHeaderParts {

    private String scheme;
    private String parameters;

    public AuthorizationHeaderParts(final String scheme, final String parameters) {
        this.scheme = scheme;
        this.parameters = parameters;
    }

    public String getScheme() {
        return scheme;
    }

    public String getParameters() {
        return parameters;
    }
}
