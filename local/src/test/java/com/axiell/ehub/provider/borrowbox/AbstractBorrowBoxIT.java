package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import org.junit.Before;
import org.mockito.Mock;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.*;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;

public class AbstractBorrowBoxIT extends AbstractContentProviderIT {
    protected static final String API_BASE_URL_VALUE = "http://api.borrowbox.com";

    /* Test */
    protected static final String LIBRARY_ID = "libraryId";
    protected static final String SECRET_KEY = "secretKey";

    protected static final String RECORD_ID_EBOOK = "recordIdEBook";
    protected static final String RECORD_ID_EAUDIO = "recordIdEAudio";

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

    protected void givenApiBaseUrl() {
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(API_BASE_URL_VALUE);
    }

    protected void givenSecretKey() {
        given(contentProviderConsumer.getProperty(BORROWBOX_SECRET_KEY)).willReturn(SECRET_KEY);
    }

    protected void givenLibraryId() {
        given(contentProviderConsumer.getProperty(BORROWBOX_LIBRARY_ID)).willReturn(LIBRARY_ID);
    }
}
