package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.PATRON_API_BASE_URL;
import static org.mockito.BDDMockito.given;

public abstract class AbstractOverDriveIT extends AbstractContentProviderIT {
    // Product #1
    protected static final String PRODUCT_ID = "B9970CE4-DF79-423F-B5DF-E2C04C1257B9";

    // Product #2
    // protected static final String PRODUCT_ID = "A43F3999-8237-461C-83FE-1F70589BD3F4";

    protected static final String INVALID_VALUE = "invalid";
    protected static final String BASE_URL = "http://api.overdrive.com";
    protected static final String OAUTH_URL = "https://oauth.overdrive.com/token";
    protected static final String FORMAT_TYPE = "ebook-overdrive";

    // Card #1
    private static final String LIBRARY_CARD = "D0200000000000";
    private static final String PIN = "2053";

    // Card #2
    //private static final String LIBRARY_CARD = "D4000000255248";
    //private static final String PIN = "1277";

    // Card #3
    // protected static final String LIBRARY_CARD = "D4000000416455";
    // protected static final String PIN = "2379";

    // Card #4
    //protected static final String LIBRARY_CARD = "D4000000358164";
    // protected static final String PIN = "1030";

    protected static final String OAUTH_PATRON_URL = "https://oauth-patron.overdrive.com/patrontoken";
    protected static final String PATRON_BASE_URL = "http://patron.api.overdrive.com";

    protected static final String WEBSITE_ID = "100300";
    protected static final String ILS_NAME = "dublinapitest";

    private static final String ERROR_PAGE_URL = "ErrorPageurl";
    private static final String READ_AUTH_URL = "OdreadAuthUrl";
    private static final String CLIENT_KEY = "AXIELLGROUPAB";
    private static final String CLIENT_SECRET = "eP74yZCSux0NuSksanK25gD-wxj230Sr";

    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;
    protected OAuthAccessToken accessToken;
    protected IOverDriveFacade underTest;
    protected Product product;

    @Before
    public void setUpOverDriveFacade() {
        underTest = new OverDriveFacade();
    }

    protected void givenContentProvider() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    protected void givenClientKey() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_KEY)).willReturn(CLIENT_KEY);
    }

    protected void givenClientSecret() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_SECRET)).willReturn(CLIENT_SECRET);
    }

    protected void thenAccessTokenIsNotNull() {
        Assert.assertNotNull(accessToken);
    }

    protected void thenAccessTokenValueIsNotNull() {
        Assert.assertNotNull(accessToken.getAccessToken());
    }

    protected void thenTokenTypeIsNotNull() {
        Assert.assertNotNull(accessToken.getTokenType());
    }

    protected void givenOAuthAccessToken() {
        givenContentProvider();
        givenOAuthUrl();
        givenClientKey();
        givenClientSecret();
        whenGetOAuthAccessToken();
        thenAccessTokenIsNotNull();
        thenAccessTokenValueIsNotNull();
        thenTokenTypeIsNotNull();
    }

    protected void givenOAuthUrl() {
        given(contentProvider.getProperty(ContentProviderPropertyKey.OAUTH_URL)).willReturn(OAUTH_URL);
    }

    protected void givenApiBaseUrl() {
        given(contentProvider.getProperty(ContentProviderPropertyKey.API_BASE_URL)).willReturn(BASE_URL);
    }

    protected void whenGetOAuthAccessToken() {
        accessToken = underTest.getOAuthAccessToken(contentProviderConsumer);
    }

    protected void whenGetProduct() {
        product = underTest.getProduct(contentProviderConsumer, PRODUCT_ID);
    }

    protected void givenPatronAccessToken() {
        givenContentProvider();
        givenClientKey();
        givenClientSecret();
        givenOAuthPatronUrl();
        givenWebSiteId();
        givenIlsName();
        whenGetPatronAccessToken();
        thenAccessTokenIsNotNull();
        thenAccessTokenValueIsNotNull();
        thenTokenTypeIsNotNull();
    }

    private void givenOAuthPatronUrl() {
        given(contentProvider.getProperty(ContentProviderPropertyKey.OAUTH_PATRON_URL)).willReturn(OAUTH_PATRON_URL);
    }

    private void givenWebSiteId() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDIRVE_WEBSITE_ID)).willReturn(WEBSITE_ID);
    }

    private void givenIlsName() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_ILS_NAME)).willReturn(ILS_NAME);
    }

    private void whenGetPatronAccessToken() {
        accessToken = underTest.getPatronOAuthAccessToken(contentProviderConsumer, LIBRARY_CARD, PIN);
    }

    protected void givenPatronApiBaseUrl() {
        given(contentProvider.getProperty(PATRON_API_BASE_URL)).willReturn(PATRON_BASE_URL);
    }

    protected void givenErrorPageUrl() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_ERROR_PAGE_URL)).willReturn(ERROR_PAGE_URL);
    }

    protected void givenReadAuthUrl() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_READ_AUTH_URL)).willReturn(READ_AUTH_URL);
    }
}
