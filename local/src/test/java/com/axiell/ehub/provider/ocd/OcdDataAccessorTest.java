package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class OcdDataAccessorTest extends ContentProviderDataAccessorTestFixture<OcdDataAccessor> {
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
        ReflectionTestUtils.setField(underTest, "ocdCheckoutHandler", ocdCheckoutHandler);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "ocdFormatHandler", ocdFormatHandler);
    }

    @Test
    public void getFormats_success() {
        givenLanguageInCommandData();
        givenFormatHandlerReturnsContentProviderFormat();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderAliasInCommandData();
        givenFormatDecorationInContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatInFormatFactory();
        whenGetIssues();
        thenActualFormatsContainsOneFormat();
        thenActualFormatEqualsExpected();
    }

    private void givenFormatHandlerReturnsContentProviderFormat() {
        given(ocdFormatHandler.getContentProviderFormat(contentProviderConsumer, getContentProviderName(), RECORD_ID)).willReturn(FORMAT_ID);
    }

    @Test
    public void getFormats_MediaNotFound() {
        givenContentProviderRecordIdInCommandData();
        givenFormatDecorationInContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatInFormatFactory();
        whenGetIssues();
        thenFormatsEmpty();
    }

    @Test
    public void createLoan_success() {
        givenContentProviderConsumerInCommandData();
        givenFormatDecorationInContentProvider();
        givenContentProviderRecordIdInCommandData();
        givenCheckout();
        givenCompleteCheckout();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    public void givenCheckout() {
        given(ocdCheckoutHandler.checkout(any(CommandData.class))).willReturn(checkout);
    }

    public void givenCheckoutFailure() {
        given(ocdCheckoutHandler.checkout(any(CommandData.class))).willThrow(InternalServerErrorException.class);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getDownloadUrls()).willReturn(Collections.singletonList(CONTENT_HREF));
        given(ocdCheckoutHandler.getCheckout(any(CommandData.class), anyString())).willReturn(checkout);
    }

    @Test(expected = InternalServerErrorException.class)
    public void createLoan_unsuccessful() {
        givenCheckoutFailure();
        givenInternalServerErrorException();
        whenCreateLoan();
    }

    private void givenInternalServerErrorException() {
        given(ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(any(ContentProviderConsumer.class),
                any(ErrorCauseArgumentType.class), anyString())).willReturn(internalServerErrorException);
    }

    @Test
    public void getContent() {
        givenCompleteCheckout();
        givenContentProviderLoanMetadataInCommandData();
        givenFormatDecorationInCommandData();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_OCD;
    }
}
