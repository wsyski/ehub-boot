package com.axiell.ehub;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.provider.record.Record;
import com.axiell.authinfo.AuthInfo;

public interface IEhubService {

    boolean isValidAlias(String alias);

    CheckoutMetadata findCheckoutByLmsLoanId(AuthInfo authInfo, String lmsLoanId, String language) throws EhubException;

    Checkout getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) throws EhubException;

    Checkout checkout(AuthInfo authInfo, Fields fields, String language) throws EhubException;

    Record getRecord(AuthInfo authInfo, String contentProviderAlias, String contentProviderRecordId, String language) throws EhubException;
}
