package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import org.junit.Before;
import org.mockito.Mock;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OCD_BASIC_TOKEN;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OCD_LIBRARY_ID;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;

public class AbstractOcdIT extends AbstractContentProviderIT {
    protected static final String API_BASE_URL_VALUE = "http://api.oneclickdigital.eu";
    protected static final String CARD = "20126001163574";
    protected static final String PIN = "1234";


    protected static final String BASIC_TOKEN = "E89CB7B3-82CF-48D0-9657-0437B5161D5E";
    protected static final String LIBRARY_ID = "4047";

    protected static final String RECORD_ID_EBOOK = "9781781858417";
    protected static final String RECORD_ID_EAUDIO = "9781407445496";

    protected static final String FORMAT_ID_EAUDIO = "eAudio";
    protected static final String FORMAT_ID_EBOOK = "eBook";

    protected OcdFacade underTest;
    @Mock
    protected Patron patron;

    @Before
    public void setUpUnderTest() {
        underTest = new OcdFacade();
        customSetUp();
    }

    protected void customSetUp() {
    }

    protected void givenApiBaseUrl() {
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(API_BASE_URL_VALUE);
    }

    protected void givenBasicToken() {
        given(contentProviderConsumer.getProperty(OCD_BASIC_TOKEN)).willReturn(BASIC_TOKEN);
    }

    protected void givenLibraryId() {
        given(contentProviderConsumer.getProperty(OCD_LIBRARY_ID)).willReturn(LIBRARY_ID);
    }
}
