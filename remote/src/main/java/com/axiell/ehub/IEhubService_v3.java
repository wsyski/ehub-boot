package com.axiell.ehub;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata_v3;
import com.axiell.ehub.provider.record.Record_v3;
import com.axiell.ehub.security.AuthInfo;

public interface IEhubService_v3 {

    CheckoutMetadata_v3 findCheckoutByLmsLoanId(AuthInfo authInfo, String lmsLoanId, String language) throws EhubException;

    Checkout getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) throws EhubException;

    Checkout checkout(AuthInfo authInfo, Fields fields, String language) throws EhubException;

    Record_v3 getRecord(AuthInfo authInfo, String contentProviderAlias, String contentProviderRecordId, String language) throws EhubException;
}
