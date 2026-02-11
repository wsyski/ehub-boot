package com.axiell.ehub.it.remote.ep.lpp;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.it.config.EhubServiceClientConfig;
import com.axiell.ehub.it.config.TestDataServiceClientConfig;
import com.axiell.ehub.it.remote.ep.RemoteEpCheckoutITFixture;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.test.config.TestDataRestApiServerConfig;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, TestDataRestApiServerConfig.class, EhubServiceClientConfig.class, TestDataServiceClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@DirtiesContext
public class RemoteLppCheckoutIT extends RemoteEpCheckoutITFixture {

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
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.TEST_EP_FORMAT_ID_1, false);
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
        return true;
    }


}
