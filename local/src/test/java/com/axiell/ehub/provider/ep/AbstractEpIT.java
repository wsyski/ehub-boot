package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import org.junit.Before;
import org.mockito.Mock;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;

public class AbstractEpIT extends AbstractContentProviderIT {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    protected static final String API_BASE_URL_VALUE = "http://provider.com/ehub-api";

    protected static final long EHUB_CONSUMER_ID = 1L;
    protected static final String EP_SITE_ID = "siteId";
    protected static final String EP_SECRET_KEY = "secretKey";

    protected static final String RECORD_ID = "recordId";

    protected static final String FORMAT_ID = "formatId";

    protected EpFacade underTest;
    @Mock
    protected Patron patron;

    @Before
    public void setUpUnderTest() {
        underTest = new EpFacade();
    }

    protected void givenConfigurationProperties() {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(API_BASE_URL_VALUE);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID)).willReturn(EP_SITE_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY)).willReturn(
                EP_SECRET_KEY);
    }


}
