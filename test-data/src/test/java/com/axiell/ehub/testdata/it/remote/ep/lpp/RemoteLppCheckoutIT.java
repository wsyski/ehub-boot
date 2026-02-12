package com.axiell.ehub.testdata.it.remote.ep.lpp;

import com.axiell.ehub.local.EhubApplication;
import com.axiell.ehub.common.EhubWebApplicationException;
import com.axiell.ehub.client.IEhubServiceClient;
import com.axiell.ehub.common.checkout.Checkout;
import com.axiell.ehub.common.checkout.CheckoutMetadata;
import com.axiell.ehub.testdata.it.config.EhubServiceClientConfig;
import com.axiell.ehub.testdata.it.config.TestDataServiceClientConfig;
import com.axiell.ehub.testdata.it.remote.ep.RemoteEpCheckoutITFixture;
import com.axiell.ehub.testdata.ITestDataServiceClient;
import com.axiell.ehub.testdata.TestDataConstants;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, EhubServiceClientConfig.class, TestDataServiceClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class RemoteLppCheckoutIT extends RemoteEpCheckoutITFixture {

    @Getter
    @Autowired
    private IEhubServiceClient ehubServiceClient;
    @Getter
    @Autowired
    private ITestDataServiceClient testDataServiceClient;

    @Test
    public final void checkoutWithExistingContentProviderLoanAndNewFormat() throws EhubWebApplicationException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutTestActiveLoanResponse(isLoanPerProduct(), TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutResponse(TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_ID_1, false);
    }

    @Test
    public final void checkoutWithNewContentProviderLoan() throws EhubWebApplicationException {
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutTestNewLoanResponse(isLoanPerProduct(), TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenLmsCheckoutResponse(TestDataConstants.LMS_RECORD_ID, TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenContentProviderCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_ID_1, true);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubWebApplicationException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_ID_0, false);
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubWebApplicationException {
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
