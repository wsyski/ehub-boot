package com.axiell.ehub.provider.ep;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;
import static com.axiell.ehub.ErrorCauseArgumentType.ALREADY_ON_LOAN;
import static com.axiell.ehub.ErrorCauseArgumentType.INVALID_CONTENT_PROVIDER_RECORD_ID;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractEpIT<F extends IEpFacade, C extends ICheckoutDTO> extends AbstractContentProviderIT {
    private static final long EHUB_CONSUMER_ID = 1L;
    private static final String PATRON_ID = "patronId";
    private static final String LIBRARY_CARD = "D0200000000000";
    protected static final String INVALID_RECORD_ID = "invalidRecordId";
    protected static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    protected RecordDTO record;

    protected C checkout;

    protected F underTest;

    @Mock
    protected Patron patron;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        underTest = createEpFacade();
    }

    @After
    public void tearDown() {
        deleteCheckout();
    }

    @Test
    public void getFormats() throws IFinder.NotFoundException {
        givenLibraryCardInPatron();
        //givenPatronIdInPatron();
        givenConfigurationProperties(EpUserIdValue.LIBRARY_CARD);
        givenContentProvider();
        givenEhubConsumer();
        whenGetFormats(getRecordId());
        thenExpectedGetFormatsResponse();
    }

    @Test
    public void getFormatsForInvalidRecordId() throws IFinder.NotFoundException {
        givenLibraryCardInPatron();
        //givenPatronIdInPatron();
        givenConfigurationProperties(EpUserIdValue.LIBRARY_CARD);
        givenContentProvider();
        givenEhubConsumer();
        thenGetFormatWithInvalidRecordIdFails();
    }

    private void thenGetFormatWithInvalidRecordIdFails() {
        whenGetFormats(INVALID_RECORD_ID);
        givenExpectedEhubException(ErrorCause.CONTENT_PROVIDER_ERROR.toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, CONTENT_PROVIDER_TEST_EP),
                        new ErrorCauseArgument(CONTENT_PROVIDER_STATUS, INVALID_CONTENT_PROVIDER_RECORD_ID.name())));
    }

    protected void givenConfigurationProperties(final EpUserIdValue epUserIdValue) {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
        given(contentProvider.isLoanPerProduct()).willReturn(isLoanPerProduct());
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(getApiBaseUri());
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID)).willReturn(getSiteId());
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY)).willReturn(getSecretKey());
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_USER_ID_VALUE))
                .willReturn(epUserIdValue.name());
    }

    protected void thenCheckoutHasTransactionId() {
        assertNotNull(checkout.getId());
    }

    protected void thenCheckoutHasExpirationDate() {
        assertNotNull(checkout.getExpirationDate());
    }

    protected void givenPatronIdInPatron() {
        given(patron.hasId()).willReturn(true);
        given(patron.getId()).willReturn(PATRON_ID);
    }

    protected void givenLibraryCardInPatron() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(LIBRARY_CARD);
    }

    private void whenGetFormats(final String recordId) {
        record = underTest.getRecord(contentProviderConsumer, patron, recordId);
    }

    private void thenExpectedGetFormatsResponse() throws IFinder.NotFoundException {
        getFormatIds().forEach(contentProviderFormatId -> {
            IMatcher<FormatDTO> matcher = new FormatIdFormatMatcher(contentProviderFormatId);
            FormatDTO format = new CollectionFinder<FormatDTO>().find(matcher, record.getFormats());
            Assert.assertThat(contentProviderFormatId, Matchers.is(format.getId()));
        });

    }

    private void deleteCheckout() {
        if (checkout != null) {
            deleteCheckout(checkout.getId());
            checkout = null;
        }
    }

    protected void deleteCheckout(final String checkoutId) {
        underTest.deleteCheckout(contentProviderConsumer, patron, checkoutId);
    }

    protected void givenExpectedEhubException(final EhubError ehubError) {
        expectedException.expect(EhubException.class);
        expectedException.expectMessage(ehubError.getMessage());
    }


    protected abstract boolean isLoanPerProduct();

    protected abstract F createEpFacade();

    protected abstract List<String> getFormatIds();

    protected abstract String getApiBaseUri();

    protected abstract String getSiteId();

    protected abstract String getSecretKey();

    protected abstract String getRecordId();
}
