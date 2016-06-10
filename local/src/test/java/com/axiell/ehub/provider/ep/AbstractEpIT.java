package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;

public abstract class AbstractEpIT<F extends IEpFacade> extends AbstractContentProviderIT {
    protected static final String API_BASE_URL_VALUE = "https://xyzaeh.ulverscroftdigital.com";
    protected static final long EHUB_CONSUMER_ID = 1L;
    protected static final String EP_SITE_ID = "1111";
    protected static final String EP_SECRET_KEY = "1111";
    protected static final String RECORD_ID = "9781407941011";
    protected static final String FORMAT_ID_0 = "mp3";
    protected static final String FORMAT_ID_1 = "m4b";
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private static final String PATRON_ID = "patronId";
    private static final String LIBRARY_CARD = "D0200000000000";
    private static final boolean IS_LOAN_PER_PRODUCT = true;

    protected RecordDTO record;

    protected F underTest;
    @Mock
    protected Patron patron;

    @Before
    public void setUpUnderTest() {
        underTest = createEpFacade();
    }

    @Ignore
    @Test
    public void getFormats() throws IFinder.NotFoundException {
        givenLibraryCardInPatron();
        //givenPatronIdInPatron();
        givenConfigurationProperties(EpUserIdValue.LIBRARY_CARD);
        givenContentProvider();
        givenEhubConsumer();
        whenGetFormats();
        thenExpectedMediaType(FORMAT_ID_0);
    }

    protected void givenConfigurationProperties(final EpUserIdValue epUserIdValue) {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
        given(contentProvider.isLoanPerProduct()).willReturn(IS_LOAN_PER_PRODUCT);
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

    private void whenGetFormats() {
        record = underTest.getRecord(contentProviderConsumer, patron, RECORD_ID);
    }

    private void thenExpectedMediaType(final String contentProviderFormatId) throws IFinder.NotFoundException {
        IMatcher<FormatDTO> matcher = new FormatIdFormatMatcher(contentProviderFormatId);
        FormatDTO format = new CollectionFinder<FormatDTO>().find(matcher, record.getFormats());
        Assert.assertThat(contentProviderFormatId, Matchers.is(format.getId()));
    }

    protected abstract F createEpFacade();
}
