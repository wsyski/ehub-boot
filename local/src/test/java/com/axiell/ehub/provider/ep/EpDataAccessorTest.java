package com.axiell.ehub.provider.ep;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class EpDataAccessorTest extends ContentProviderDataAccessorTestFixture {

    private EpDataAccessor underTest;
    @Mock
    private EpFacade epFacade;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private RecordDTO record;
    @Mock
    private FormatDTO format;
    @Mock
    private CheckoutDTO checkout;

    @Before
    public void setUp() {
        underTest = new EpDataAccessor();
        ReflectionTestUtils.setField(underTest, "epFacade", epFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
    }

    @Test
    public void getFormats() {
        givenLanguageInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderAliasInCommandData();
        givenPatronInCommandData();
        givenEpFacadeReturnsFormats();
        givenFormatDecorationFromContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatFromFormatFactory();
        whenGetFormats();
        thenFormatSetContainsOneFormat();
        thenActualFormatEqualsExpected();
    }

    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProviderRecordIdInCommandData();
        givenCompleteCheckout();
        givenCheckout();
        givenContentLink();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    @Test
    public void getContent() {
        givenGetCheckout();
        givenCompleteCheckout();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenContentProviderLoanMetadataInCommandData();
        givenContentLink();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    private void givenEpFacadeReturnsFormats() {
        given(epFacade.getRecord(contentProviderConsumer, patron, RECORD_ID)).willReturn(record);
        given(record.getFormats()).willReturn(Collections.singletonList(format));
        given(format.getId()).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    public void givenCheckout() {
        given(epFacade.checkout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class), any(String.class))).willReturn(checkout);
    }

    public void givenGetCheckout() {
        given(epFacade.getCheckout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class))).willReturn(checkout);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
        given(checkout.getContentLinks()).willReturn(ContentLinkBuilder.defaultContentLinks().toDTO());
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    public void whenGetContent() {
        actualContentLink = underTest.getContent(commandData).getContentLinks().getContentLinks().get(0);
    }

    @Override
    protected String getContentProviderName() {
        return CONTENT_PROVIDER_TEST_EP;
    }
}
