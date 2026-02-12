package com.axiell.ehub.client;

import com.axiell.ehub.common.EhubWebApplicationException;
import com.axiell.ehub.common.Fields;
import com.axiell.ehub.common.checkout.Checkout;
import com.axiell.ehub.common.checkout.CheckoutMetadata;
import com.axiell.ehub.common.provider.record.Record;
import com.axiell.authinfo.AuthInfo;

public interface IEhubServiceClient {

    boolean isValidAlias(String alias);

    CheckoutMetadata findCheckoutByLmsLoanId(AuthInfo authInfo, String lmsLoanId, String language) throws EhubWebApplicationException;

    Checkout getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) throws EhubWebApplicationException;

    Checkout checkout(AuthInfo authInfo, Fields fields, String language) throws EhubWebApplicationException;

    Record getRecord(AuthInfo authInfo, String contentProviderAlias, String contentProviderRecordId, String language) throws EhubWebApplicationException;
}
