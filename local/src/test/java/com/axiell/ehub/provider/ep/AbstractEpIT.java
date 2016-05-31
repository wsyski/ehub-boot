package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ep.lpf.EpFacade;
import com.axiell.ehub.provider.ep.lpf.IEpFacade;
import org.junit.Before;
import org.mockito.Mock;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;

public class AbstractEpIT extends AbstractContentProviderIT {
    protected static final String API_BASE_URL_VALUE = "https://xyzaeh.ulverscroftdigital.com";
    protected static final long EHUB_CONSUMER_ID = 1L;
    protected static final String EP_SITE_ID = "1111";
    protected static final String EP_SECRET_KEY = "1111";
    protected static final String RECORD_ID = "9781407941011";
    protected static final String FORMAT_ID = "eAudio";
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private static final String PATRON_ID = "patronId";
    private static final String LIBRARY_CARD = "D0200000000000";


    protected IEpFacade underTest;
    @Mock
    protected Patron patron;

    @Before
    public void setUpUnderTest() {
        underTest = new EpFacade();
    }

    protected void givenConfigurationProperties(final EpUserIdValue epUserIdValue) {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(API_BASE_URL_VALUE);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID)).willReturn(EP_SITE_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY)).willReturn(EP_SECRET_KEY);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_USER_ID_VALUE))
                .willReturn(epUserIdValue.name());
    }

    protected void givenPatronIdInPatron() {
        given(patron.hasId()).willReturn(true);
        given(patron.getId()).willReturn(PATRON_ID);
    }

    protected void givenLibraryCardInPatron() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(LIBRARY_CARD);
    }
}
