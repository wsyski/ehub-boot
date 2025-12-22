package com.axiell.ehub.it.ep.lpf;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.it.ep.RemoteEpCheckoutITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RemoteLpfCheckoutIT extends RemoteEpCheckoutITFixture {

    @Test
    public final void checkoutWithExistingContentProviderLoanAndNewFormat() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutTestActiveLoanResponse(isLoanPerProduct(), TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutResponse(TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenContentProviderGetCheckoutResponse();
        Exception exception = Assertions.assertThrows(EhubException.class, () -> {
            Checkout checkout = whenCheckout();
        });

        thenExpectedEhubException(exception, InternalServerErrorException.class, ErrorCause.CONTENT_PROVIDER_UNSUPPORTED_LOAN_PER_PRODUCT,
                new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, TestDataConstants.CONTENT_PROVIDER_TEST_EP));

    }

    @Test
    public final void checkoutWithNewContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutTestNewLoanResponse(isLoanPerProduct(), TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutResponse(TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
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
        return false;
    }
}
