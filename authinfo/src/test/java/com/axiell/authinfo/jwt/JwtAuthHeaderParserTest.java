package com.axiell.authinfo.jwt;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.Patron;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class JwtAuthHeaderParserTest {
    private static final long EXPIRATION_TIME_IN_SECONDS = 0L;
    private static final long ARENA_AGENCY_MEMBER_ID = 2000L;
    private static final String SITE_ID = "3000";
    private static final long EHUB_CONSUMER_ID = 4000L;
    private static final String PATRON_ID = "patronId";
    private static final String NAME = "name";
    private static final long USER_ID = 10L;
    private static final String EMAIL = "email";
    private static final String PIN = "pin";
    private static final String LIBRARY_CARD = "libraryCard";
    private static final String SECRET_KEY = "c2VjcmV0S2V5"; // base64 encoded secretKey

    private JwtAuthHeaderParser underTest;
    private AuthInfo authInfo;

    @Before
    public void setUp() {
        ConstantAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver = new ConstantAuthHeaderSecretKeyResolver();
        authHeaderSecretKeyResolver.setSecretKey(SECRET_KEY);
        authHeaderSecretKeyResolver.setExpirationTimeInSeconds(EXPIRATION_TIME_IN_SECONDS);
        authHeaderSecretKeyResolver.setValidate(true);
        underTest = new JwtAuthHeaderParser();
        underTest.setAuthHeaderSecretKeyResolver(authHeaderSecretKeyResolver);
        Patron patron = new Patron.Builder().arenaUserId(USER_ID).email(EMAIL).name(NAME).id(PATRON_ID).libraryCard(LIBRARY_CARD).pin(PIN).build();
        authInfo = new AuthInfo.Builder().arenaAgencyMemberId(ARENA_AGENCY_MEMBER_ID).siteId(SITE_ID).ehubConsumerId(EHUB_CONSUMER_ID).patron(patron).build();
    }

    @Test
    public void serializeAndParse() {
        String authorizationHeader = underTest.serialize(authInfo);
        AuthInfo actualAuthInfo = underTest.parse(authorizationHeader);
        Assert.assertThat(actualAuthInfo, is(actualAuthInfo));
    }
}