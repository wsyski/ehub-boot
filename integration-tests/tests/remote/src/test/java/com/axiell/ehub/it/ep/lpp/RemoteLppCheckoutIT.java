package com.axiell.ehub.it.ep.lpp;

import com.axiell.ehub.*;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.it.ep.RemoteEpCheckoutITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Test;

public class RemoteLppCheckoutIT extends RemoteEpCheckoutITFixture {


    @Test
    public final void checkoutWithExistingContentProviderLoanAndNewFormat() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_ID_1, false);
    }

    @Test
    public final void checkoutWithNewContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_ID_1, true);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_ID_0, false);
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenContentProviderGetCheckoutResponse();
        CheckoutMetadata checkoutMetadata = whenFindCheckoutMetadataByLmsLoandId();
        thenValidCheckoutMetadata(checkoutMetadata, false);
    }

    @Override
    protected String getContentProviderName() {
        return TestDataConstants.CONTENT_PROVIDER_TEST_EP;
    }

    @Override
    protected boolean isLoanPerProduct() {
        return true;
    }
}
