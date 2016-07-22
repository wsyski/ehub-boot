package com.axiell.ehub.it.ep.lpf;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.it.ep.RemoteEpLoanITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Test;

public class RemoteLpfLoanIT extends RemoteEpLoanITFixture {

    @Test
    public final void checkoutWithExistingContentProviderLoanAndNewFormat() throws EhubException {
        givenExpectedEhubException(InternalServerErrorException.class,ErrorCause.CONTENT_PROVIDER_UNSUPPORTED_LOAN_PER_PRODUCT,
                new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, TestDataConstants.CONTENT_PROVIDER_TEST_EP));
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_1_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
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
        return false;
    }
}
