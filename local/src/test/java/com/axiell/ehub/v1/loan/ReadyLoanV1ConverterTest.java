package com.axiell.ehub.v1.loan;

import com.axiell.ehub.checkout.*;
import com.axiell.ehub.loan.*;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ReadyLoanV1ConverterTest {
    private static final String URL = "url";
    private CheckoutMetadata checkoutMetadataWithStreamingFormat = CheckoutMetadataBuilder.checkoutMetadataWithStreamingFormat();
    private CheckoutMetadata checkoutMetadataWithDownloadableFormat = CheckoutMetadataBuilder.checkoutMetadataWithDownloadableFormat();
    @Mock
    private Checkout checkout;
    @Mock
    private LmsLoan lmsLoan;
    @Mock
    private ContentLink contentLink;
    private ReadyLoan_v1 actualReadyLoan_v1;
    private ContentProviderLoan_v1 actualContentProviderLoan_v1;

    @Test
    public void convert_streamingContent() {
        givenContentLink();
        given(checkout.metadata()).willReturn(checkoutMetadataWithStreamingFormat);
        whenConvert();
        thenActualReadyLoanIdEqualsExpected();
        thenActualContentProviderLoanEqualsExpected();
        thenActualExpirationDateEqualsExpectedExpirationDate();
        thenActualStreamingContentEqualsExpected();
        thenActualLmsLoanEqualsExpected();
    }

    private void givenContentLink() {
        given(contentLink.href()).willReturn(URL);
        ContentLinks contentLinks = new ContentLinks(Collections.singletonList(contentLink));
        given(checkout.contentLinks()).willReturn(contentLinks);
    }

    private void whenConvert() {
        actualReadyLoan_v1 = ReadyLoanV1Converter.convert(checkout);
        actualContentProviderLoan_v1 = actualReadyLoan_v1.getContentProviderLoan();
    }

    private void thenActualReadyLoanIdEqualsExpected() {
        assertEquals(CheckoutMetadataBuilder.ID, actualReadyLoan_v1.getId());
    }

    private void thenActualContentProviderLoanEqualsExpected() {
        assertEquals(CheckoutMetadataBuilder.CONTENT_PROVIDER_LOAN_ID, actualContentProviderLoan_v1.getId());
    }

    private void thenActualExpirationDateEqualsExpectedExpirationDate() {
        assertEquals(CheckoutMetadataBuilder.EXPIRATION_DATE, actualContentProviderLoan_v1.getExpirationDate());
    }

    private void thenActualStreamingContentEqualsExpected() {
        StreamingContent_v1 actualContent = (StreamingContent_v1) actualContentProviderLoan_v1.getContent();
        assertEquals(URL, actualContent.getUrl());
        assertEquals(FormatBuilder.PLAYER_HEIGHT, actualContent.getHeight());
        assertEquals(FormatBuilder.PLAYER_WIDTH, actualContent.getWidth());
    }

    private void thenActualLmsLoanEqualsExpected() {
        LmsLoan_v1 actualLmsLoan_v1 = actualReadyLoan_v1.getLmsLoan();
        assertEquals(CheckoutMetadataBuilder.LMS_LOAN_ID, actualLmsLoan_v1.getId());
    }

    @Test
    public void convert_downloadableContent() {
        givenContentLink();
        given(checkout.metadata()).willReturn(checkoutMetadataWithDownloadableFormat);
        whenConvert();
        thenActualReadyLoanIdEqualsExpected();
        thenActualContentProviderLoanEqualsExpected();
        thenActualExpirationDateEqualsExpectedExpirationDate();
        thenActualDownloadableContentEqualsExpected();
        thenActualLmsLoanEqualsExpected();
    }

    private void thenActualDownloadableContentEqualsExpected() {
        DownloadableContent_v1 actualContent = (DownloadableContent_v1) actualContentProviderLoan_v1.getContent();
        assertEquals(URL, actualContent.getUrl());
    }
}
