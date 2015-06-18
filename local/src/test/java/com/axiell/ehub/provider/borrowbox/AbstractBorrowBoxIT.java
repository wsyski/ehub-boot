package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import org.junit.Before;
import org.mockito.Mock;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;

public class AbstractBorrowBoxIT extends AbstractContentProviderIT {
    protected static final String API_BASE_URL_VALUE = "https://lms-axiell.bolindadigital.com";

    /* Test */
    protected static final String BORROWBOX_SITE_ID = "4390";
    protected static final String BORROWBOX_LIBRARY_ID = "developmentPartner";
    protected static final String BORROWBOX_SECRET_KEY = "8271ee62ad72c5e7c7b8ddbb6898835f64d2bb93";

    protected static final String RECORD_ID_EBOOK = "9780730491590-HCP022467-000";
    protected static final String RECORD_ID_EAUDIO = "9781742672144-BOL003860-000";

    protected static final String FORMAT_ID_EAUDIO = "BorrowBox.eAudiobook";
    protected static final String FORMAT_ID_EBOOK = "BorrowBox.eBook";

    protected BorrowBoxFacade underTest;
    @Mock
    protected Patron patron;

    @Before
    public void setUpUnderTest() {
        underTest = new BorrowBoxFacade();
        customSetUp();
    }

    protected void customSetUp() {
    }

    protected void givenConfigurationProperties() {
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(API_BASE_URL_VALUE);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SITE_ID)).willReturn(BORROWBOX_SITE_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_LIBRARY_ID)).willReturn(
                BORROWBOX_LIBRARY_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SECRET_KEY)).willReturn(
                BORROWBOX_SECRET_KEY);
    }
}
