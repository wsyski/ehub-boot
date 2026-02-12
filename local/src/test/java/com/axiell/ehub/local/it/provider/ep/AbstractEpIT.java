package com.axiell.ehub.local.it.provider.ep;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.error.WebApplicationExceptionMatcher;
import com.axiell.ehub.common.util.CollectionFinder;
import com.axiell.ehub.common.util.IFinder;
import com.axiell.ehub.common.util.IMatcher;
import com.axiell.ehub.local.it.provider.AbstractContentProviderIT;
import com.axiell.ehub.local.provider.ep.EpResourceFactory;
import com.axiell.ehub.local.provider.ep.FormatDTO;
import com.axiell.ehub.local.provider.ep.ICheckoutDTO;
import com.axiell.ehub.local.provider.ep.IEpFacade;
import com.axiell.ehub.local.provider.ep.RecordDTO;
import jakarta.ws.rs.NotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.axiell.ehub.common.ErrorCauseArgumentType.INVALID_CONTENT_PROVIDER_RECORD_ID;
import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class AbstractEpIT<F extends IEpFacade, C extends ICheckoutDTO> extends AbstractContentProviderIT {
    protected static final String INVALID_RECORD_ID = "invalidRecordId";
    protected static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    protected static final long EP_TOKEN_EXPIRATION_TIME_IN_SECONDS = 86400;
    private static final long EHUB_CONSUMER_ID = 1L;
    private static final String PATRON_ID = "patronId";
    private static final String LIBRARY_CARD = "D0200000000000";
    protected RecordDTO record;

    protected C checkout;

    protected F underTest;

    @Mock
    protected Patron patron;

    @BeforeEach
    public void setUp() {
        ApplicationContext applicationContext = getApplicationContext();
        EpResourceFactory epResourceFactory = (EpResourceFactory) applicationContext.getBean("epResourceFactory");
        underTest = createEpFacade();
        ReflectionTestUtils.setField(underTest, "epResourceFactory", epResourceFactory);
    }

    @AfterEach
    public void tearDown() {
        deleteCheckout();
    }

    @Test
    public void getFormats() throws IFinder.NotFoundException {
        givenLibraryCardInPatron();
        givenPatronIdInPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenEhubConsumer();
        whenGetFormats(getRecordId());
        thenExpectedGetFormatsResponse();
    }

    @Test
    public void getFormatsForInvalidRecordId() throws IFinder.NotFoundException {
        givenLibraryCardInPatron();
        givenPatronIdInPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenEhubConsumer();
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            whenGetFormats(INVALID_RECORD_ID);
        });
        assertThat(exception, Matchers.is(new WebApplicationExceptionMatcher(NotFoundException.class, INVALID_CONTENT_PROVIDER_RECORD_ID.name())));
    }

    protected void givenConfigurationProperties() {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
        given(contentProvider.isLoanPerProduct()).willReturn(isLoanPerProduct());
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(getApiBaseUri());
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID)).willReturn(getSiteId());
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY)).willReturn(getSecretKey());
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_TOKEN_EXPIRATION_TIME_IN_SECONDS))
                .willReturn(String.valueOf(EP_TOKEN_EXPIRATION_TIME_IN_SECONDS));
    }

    protected void thenCheckoutHasTransactionId() {
        Assertions.assertNotNull(checkout.getId());
    }

    protected void thenCheckoutHasExpirationDate() {
        Assertions.assertNotNull(checkout.getExpirationDate());
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
            assertThat(contentProviderFormatId, Matchers.is(format.getId()));
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

    /*
    protected void givenExpectedWebApplicationException(final Class<? extends WebApplicationException> clazz,
                                                        final ErrorCauseArgumentType errorCauseArgumentType) {

        expectedException.expect(new WebApplicationExceptionMatcher(clazz, errorCauseArgumentType.name()));
    }
     */

    protected abstract boolean isLoanPerProduct();

    protected abstract F createEpFacade();

    protected abstract List<String> getFormatIds();

    protected abstract String getApiBaseUri();

    protected abstract String getSiteId();

    protected abstract String getSecretKey();

    protected abstract String getRecordId();
}
