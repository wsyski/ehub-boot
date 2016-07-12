package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.issue.Issue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class BorrowBoxDataAccessorTest extends ContentProviderDataAccessorTestFixture {

    private BorrowBoxDataAccessor underTest;
    @Mock
    private BorrowBoxFacade borrowBoxFacade;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private FormatsDTO formats;
    @Mock
    private FormatsDTO.FormatDTO format;
    @Mock
    private CheckoutDTO checkout;

    @Before
    public void setUp() {
        underTest = new BorrowBoxDataAccessor();
        ReflectionTestUtils.setField(underTest, "borrowBoxFacade", borrowBoxFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
    }

    @Test
    public void getFormats() {
        givenLanguageInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderAliasInCommandData();
        givenPatronInCommandData();
        givenBorrowBoxFacadeReturnsFormats();
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
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    @Test
    public void getContent() {
        givenGetCheckout();
        givenCompleteCheckout();
        givenContentProviderLoanMetadataInCommandData();
        givenFormatDecorationInCommandData();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    private void givenBorrowBoxFacadeReturnsFormats() {
        given(borrowBoxFacade.getFormats(contentProviderConsumer, patron, LANGUAGE, RECORD_ID)).willReturn(formats);
        given(formats.getFormats()).willReturn(Collections.singletonList(format));
        given(format.getFormatId()).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
        List<Issue> issues =underTest.getIssues(commandData);
        actualFormats = issues.get(0).getFormats();
    }

    public void givenCheckout() {
        given(borrowBoxFacade.checkout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class), any(String.class), any(String.class))).willReturn(checkout);
    }

    public void givenGetCheckout() {
        given(borrowBoxFacade.getCheckout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class), any(String.class))).willReturn(checkout);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getLoanId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
        given(checkout.getContentUrl()).willReturn(CONTENT_HREF);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    public void whenGetContent() {
        actualContentLink = underTest.getContent(commandData).getContentLinks().getContentLinks().get(0);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_BORROWBOX;
    }
}
