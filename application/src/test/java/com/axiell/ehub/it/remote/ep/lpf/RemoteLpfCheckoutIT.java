package com.axiell.ehub.it.remote.ep.lpf;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.it.config.EhubServiceClientConfig;
import com.axiell.ehub.it.config.TestDataServiceClientConfig;
import com.axiell.ehub.it.remote.ep.RemoteEpCheckoutITFixture;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.TestDataConstants;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {EhubApplication.class, EhubServiceClientConfig.class, TestDataServiceClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteLpfCheckoutIT extends RemoteEpCheckoutITFixture {

    @Getter
    @Autowired
    private IEhubServiceClient ehubServiceClient;
    @Getter
    @Autowired
    private ITestDataServiceClient testDataServiceClient;

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
