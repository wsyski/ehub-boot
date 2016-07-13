package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.CommandData;
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
import static org.mockito.Matchers.anyString;

public class OcdDataAccessorTest extends ContentProviderDataAccessorTestFixture {

    private OcdDataAccessor underTest;
    @Mock
    private IOcdAuthenticator ocdAuthenticator;
    @Mock
    private BearerToken bearerToken;
    @Mock
    private IOcdCheckoutHandler ocdCheckoutHandler;
    @Mock
    private IOcdFormatHandler ocdFormatHandler;
    @Mock
    private Checkout checkout;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;

    @Before
    public void setUpUnderTest() {
        underTest = new OcdDataAccessor();
        ReflectionTestUtils.setField(underTest, "ocdAuthenticator", ocdAuthenticator);
        ReflectionTestUtils.setField(underTest, "ocdCheckoutHandler", ocdCheckoutHandler);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "ocdFormatHandler", ocdFormatHandler);
    }

    @Test
    public void getFormats_success() {
        givenLanguageInCommandData();
        givenFormatHandlerReturnsContentProviderFormat();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderAliasInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatFromFormatFactory();
        whenGetFormats();
        thenFormatSetContainsOneFormat();
        thenActualFormatEqualsExpected();
    }

    private void givenFormatHandlerReturnsContentProviderFormat() {
        given(ocdFormatHandler.getContentProviderFormat(contentProviderConsumer, getContentProviderName(), RECORD_ID)).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
        List<Issue> issues =underTest.getIssues(commandData);
        actualFormats = issues.get(0).getFormats();
    }

    @Test
    public void getFormats_MediaNotFound() {
        givenContentProviderRecordIdInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatFromFormatFactory();
        whenGetFormats();
        thenFormatSetIsEmpty();
    }

    @Test
    public void createLoan_success() {
        givenBearerToken();
        givenContentProviderConsumerInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProviderRecordIdInCommandData();
        givenCheckoutIsSuccessful();
        givenCheckout();
        givenCompleteCheckout();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    private void givenBearerToken() {
        given(ocdAuthenticator.authenticate(commandData)).willReturn(bearerToken);
    }

    public void givenCheckout() {
        given(ocdCheckoutHandler.checkout(any(BearerToken.class), any(CommandData.class))).willReturn(checkout);
    }

    public void givenCheckoutIsSuccessful() {
        given(checkout.isSuccessful()).willReturn(true);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getDownloadUrls()).willReturn(Collections.singletonList(CONTENT_HREF));
        given(ocdCheckoutHandler.getCompleteCheckout(any(BearerToken.class), any(CommandData.class), anyString())).willReturn(checkout);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    @Test(expected = InternalServerErrorException.class)
    public void createLoan_unsuccessful() {
        givenBearerToken();
        givenInternalServerErrorException();
        givenCheckout();
        whenCreateLoan();
    }

    private void givenInternalServerErrorException() {
        given(ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(any(ContentProviderConsumer.class), any(ErrorCauseArgumentType.class), anyString())).willReturn(internalServerErrorException);
    }

    @Test
    public void getContent() {
        givenBearerToken();
        givenCompleteCheckout();
        givenContentProviderLoanMetadataInCommandData();
        givenFormatDecorationInCommandData();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    public void whenGetContent() {
        actualContentLink = underTest.getContent(commandData).getContentLinks().getContentLinks().get(0);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_OCD;
    }
}
