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
    protected static final String API_BASE_URL_VALUE = "http://api.oneclickdigital.us";

    /* Test */
    protected static final String BASIC_TOKEN = "e61459ee-a96d-436a-91c8-e5313c47d9e4";
    protected static final String LIBRARY_ID = "504";

    /* Bedfordshire */
    //protected static final String BASIC_TOKEN = "e89cb7b3-82cf-48d0-9657-0437b5161d5e";
    //protected static final String LIBRARY_ID = "3511";

    protected static final String EBOOK_TITLE_ID = "65690";
    protected static final String AUDIO_TITLE_ID = "223";
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
