package com.axiell.ehub.it.lpp;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.it.RemoteLoanITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Test;

public class LppRemoteLoanIT extends RemoteLoanITFixture {

    @Test
    public final void checkoutWithContentProviderError() throws EhubException {
        givenExpectedEhubException(ErrorCause.CONTENT_PROVIDER_ERROR.toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, TestDataConstants.CONTENT_PROVIDER_TEST_EP),
                new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS, ErrorCauseArgumentValue.Type.ALREADY_ON_LOAN)));
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderCheckoutErrorResponse(ErrorCauseArgumentValue.Type.ALREADY_ON_LOAN);
        Checkout checkout = whenCheckout();
    }

    @Test
    public final void checkoutWithExistingContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_1_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_1_ID);
    }

    @Test
    public final void checkoutWithExistingContentProviderLoanAndNewFormat() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_0_ID);
    }

    @Test
    public final void checkoutWithNewContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_0_ID);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_1_ID);
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenContentProviderGetCheckoutResponse();
        CheckoutMetadata checkoutMetadata = whenFindCheckoutMetadataByLmsLoandId();
        thenValidCheckoutMetadata(checkoutMetadata);
    }

    protected boolean isLoanPerProduct() {
        return true;
    }
}
