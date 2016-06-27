package com.axiell.ehub.it.lpf;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.Fields;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.it.RemoteITFixture;
import com.axiell.ehub.it.RemoteLoanITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThat;

public class LpfRemoteLoanIT extends RemoteLoanITFixture {

    @Test
    public final void checkoutWithExistingContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_1_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout,TestDataConstants.TEST_EP_FORMAT_1_ID, false);
    }

    @Test
    public final void checkoutWithExistingContentProviderLoanAndNewFormat() throws EhubException {
        givenExpectedEhubException(ErrorCause.CONTENT_PROVIDER_UNSUPPORTED_LOAN_PER_PRODUCT
                .toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, TestDataConstants.CONTENT_PROVIDER_TEST_EP)));
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
    }

    @Test
    public final void checkoutWithNewContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_0_ID, true);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_1_ID, false);
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenContentProviderGetCheckoutResponse();
        CheckoutMetadata checkoutMetadata = whenFindCheckoutMetadataByLmsLoandId();
        thenValidCheckoutMetadata(checkoutMetadata, false);
    }

    protected boolean isLoanPerProduct() {
        return false;
    }
}
