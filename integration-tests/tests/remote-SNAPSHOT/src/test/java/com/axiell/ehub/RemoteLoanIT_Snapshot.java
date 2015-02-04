package com.axiell.ehub;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Before;

public class RemoteLoanIT_Snapshot extends AbstractRemoteLoanIT {
    private IEhubService ehubService;
    private Fields fields;

    @Before
    public void initFields() {
        fields = new Fields();
        fields.addValue("lmsRecordId", TestDataConstants.LMS_RECORD_ID);
        fields.addValue("contentProviderName", TestDataConstants.LMS_RECORD_ID);
        fields.addValue("contentProviderRecordId", TestDataConstants.ELIB_RECORD_0_ID);
        fields.addValue("contentProviderFormatId", TestDataConstants.ELIB_FORMAT_0_ID);
    }

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected Checkout whenCheckout() throws EhubException {
        return ehubService.checkout(authInfo, fields, LANGUAGE);
    }

    @Override
    protected CheckoutMetadata whenFindCheckoutMetadataByLmsLoandId() throws EhubException {
        return ehubService.findCheckoutByLmsLoanId(authInfo, lmsLoanId, LANGUAGE);
    }

    @Override
    protected Checkout whenGetCheckoutByLoanId() throws EhubException {
        return ehubService.getCheckout(authInfo, readyLoanId, LANGUAGE);
    }
}
