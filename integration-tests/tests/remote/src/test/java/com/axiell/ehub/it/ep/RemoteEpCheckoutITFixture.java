package com.axiell.ehub.it.ep;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.checkout.SupplementLink;
import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.it.RemoteCheckoutITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class RemoteEpCheckoutITFixture extends RemoteCheckoutITFixture {

    @Test
    public final void checkoutWithContentProviderError() throws EhubException {

        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutTestNewLoanResponse(isLoanPerProduct(), TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutResponse(TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenContentProviderCheckoutErrorResponse(ErrorCauseArgumentType.ALREADY_ON_LOAN);
        Exception exception = Assertions.assertThrows(EhubException.class, () -> {
            Checkout checkout = whenCheckout();
        });

        thenExpectedContentProviderErrorException(exception, ErrorCauseArgumentType.ALREADY_ON_LOAN.name());
    }

    @Override
    protected void thenValidContentLinks(final ContentLinks contentLinks, final String contentProviderFormatId) {
        Assertions.assertNotNull(contentLinks);
        Assertions.assertEquals(2, contentLinks.getContentLinks().size());
        assertThat(contentLinks.getContentLinks().get(0),
                matchesExpectedContentLink(new ContentLink("http:/localhost:16520/ep/api/v5.0/records/recordId_0/" + contentProviderFormatId + "/content_0")));
        assertThat(contentLinks.getContentLinks().get(1),
                matchesExpectedContentLink(new ContentLink("http:/localhost:16520/ep/api/v5.0/records/recordId_0/" + contentProviderFormatId + "/content_1")));
    }

    @Override
    protected void thenValidSupplementLinks(final SupplementLinks supplementLinks, final String contentProviderFormatId) {
        Assertions.assertNotNull(supplementLinks);
        Assertions.assertEquals(2, supplementLinks.getSupplementLinks().size());
        assertThat(supplementLinks.getSupplementLinks().get(0),
                matchesExpectedSupplementLink(
                        new SupplementLink("supplement_0", "http:/localhost:16520/ep/api/v5.0/records/recordId_0/" + contentProviderFormatId + "/supplement_0")));
        assertThat(supplementLinks.getSupplementLinks().get(1),
                matchesExpectedSupplementLink(
                        new SupplementLink("supplement_1", "http:/localhost:16520/ep/api/v5.0/records/recordId_0/" + contentProviderFormatId + "/supplement_1")));
    }

    @Override
    protected String getContentProviderFormatId() {
        return TestDataConstants.TEST_EP_FORMAT_ID_0;
    }

    @Override
    protected String getIssueId() {
        return null;
    }
}
