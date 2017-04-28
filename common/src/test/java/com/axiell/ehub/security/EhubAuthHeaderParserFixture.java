package com.axiell.ehub.security;

import com.axiell.auth.AuthInfo;
import com.axiell.auth.IAuthHeaderSecretKeyResolver;
import org.junit.Before;

public abstract class EhubAuthHeaderParserFixture {
    protected static final Long EHUB_CONSUMER_ID = 254405L;
    protected static final String PATRON_ID = "2D74D1E30BD80E7FE05400144FF9C26F";
    protected static final String LIBRARY_CARD = "5007113623";
    protected static final String PIN = "5046";
    protected static final String EMAIL = "arena@axiell.com";
    protected static final String SIGNATURE = "signature";
    protected static final String SECRET_KEY = "kmmJoZ8n0b";
    protected EhubAuthHeaderParser underTest;
    protected String authorizationHeader;
    protected AuthInfo authInfo;

    @Before
    public void setUpFixture() {
        underTest = new EhubAuthHeaderParser();
        underTest.setAuthHeaderSecretKeyResolver(new AuthHeaderSecretKeyResolver());
    }

    protected static class AuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
        @Override
        public String getSecretKey(Long ehubConsumerId) {
            return SECRET_KEY;
        }
    }
}
