package com.axiell.ehub.it.lpp;

import com.axiell.ehub.*;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.it.RemoteLoanITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Test;

public class LppRemoteLoanIT extends RemoteLoanITFixture {

    @Test
    public final void checkoutWithExistingContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_0_ID, false);
    }

    @Test
    public final void checkoutWithExistingContentProviderLoanAndNewFormat() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_1_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_1_ID, false);
    }

    @Test
    public final void checkoutWithNewContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_1_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_1_ID, true);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_0_ID, false);
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
