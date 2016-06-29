package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ep.EpUserIdValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractZinioIT extends AbstractContentProviderIT {
    private static final long EHUB_CONSUMER_ID = 1L;
    private static final String EMAIL = "wos@axiell.com";
    private static final String API_BASE_URL = "http://www.rbdigitaltest.com";
    private static final String ZINIO_LIB_ID = "axielltest";
    private static final String ZINIO_TOKEN = "B5R7OfqTJaoeVeO0";
    private static final String LIBRARY_CARD = "12345";

    protected ZinioFacade underTest;

    @Mock
    protected Patron patron;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        underTest = new ZinioFacade();
    }

    protected void givenConfigurationProperties() {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(ContentProvider.CONTENT_PROVIDER_ZINIO);
        given(contentProvider.isLoanPerProduct()).willReturn(false);
        given(contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL)).willReturn(API_BASE_URL);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID)).willReturn(ZINIO_LIB_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN)).willReturn(ZINIO_TOKEN);
    }

    protected void givenPatron() {
        given(patron.hasEmail()).willReturn(true);
        given(patron.getEmail()).willReturn(EMAIL);
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(LIBRARY_CARD);
    }

    protected void givenExpectedEhubException(final EhubError ehubError) {
        expectedException.expect(EhubException.class);
        expectedException.expectMessage(ehubError.getMessage());
    }
}
