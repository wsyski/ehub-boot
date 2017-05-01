package com.axiell.auth;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;

import static com.axiell.auth.IAuthHeaderParser.BEARER_SCHEME;

public class AuthorizationHeaderParserTest {
    private static final String AUTHORIZATION_TOKEN_PARAMETERS = "name0=value0";
    private String authorizationHeader;
    private AuthorizationHeaderParser underTest;
    private AuthorizationHeaderParts authorizationHeaderParts;

    @Test
    public void authorizationHeader() {
        givenAuthorizationHeader();
        whenNewAuthHeaderParser();
        thenExpectedAuthorizationHeaderParts(BEARER_SCHEME);
    }

    @Test
    public void authorizationHeaderWithQuotes() {
        givenAuthorizationHeaderWithQuotes();
        whenNewAuthHeaderParser();
        thenExpectedAuthorizationHeaderParts(BEARER_SCHEME);
    }

    @Test
    public void authorizationHeaderWithoutScheme() {
        givenAuthorizationHeaderWithoutScheme();
        whenNewAuthHeaderParser();
        thenExpectedAuthorizationHeaderParts(null);
    }

    @Test
    public void missingAuthorizationHeader() {
        givenNoAuthorizationHeader();
        whenNewAuthHeaderParser();
        thenNullAuthorizationHeaderParts();
    }

    private void givenNoAuthorizationHeader() {
        authorizationHeader = null;
    }

    private void thenExpectedAuthorizationHeaderParts(String scheme) {
        if (scheme==null) {
            Assert.assertThat(authorizationHeaderParts.getScheme(), Matchers.nullValue());
        }
        else {
            Assert.assertThat(authorizationHeaderParts.getScheme(), Matchers.is(scheme));
        }
        Assert.assertThat(authorizationHeaderParts.getParameters(), Matchers.is(AUTHORIZATION_TOKEN_PARAMETERS));
    }

    private void thenNullAuthorizationHeaderParts() {
        Assert.assertThat(authorizationHeaderParts, Matchers.nullValue());
    }

    private void whenNewAuthHeaderParser() {
        underTest = new AuthorizationHeaderParser(authorizationHeader);
        authorizationHeaderParts = underTest.getAuthorizationHeaderParts();
    }

    private void givenAuthorizationHeader() {
        authorizationHeader = MessageFormat.format("Bearer {0}", AUTHORIZATION_TOKEN_PARAMETERS);
    }

    private void givenAuthorizationHeaderWithQuotes() {
        authorizationHeader = MessageFormat.format("Bearer \"{0}\"", AUTHORIZATION_TOKEN_PARAMETERS);
    }

    private void givenAuthorizationHeaderWithoutScheme() {
        authorizationHeader = MessageFormat.format("{0}", AUTHORIZATION_TOKEN_PARAMETERS);
    }
}