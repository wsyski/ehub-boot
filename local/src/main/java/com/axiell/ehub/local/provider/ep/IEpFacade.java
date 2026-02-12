package com.axiell.ehub.local.provider.ep;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;

public interface IEpFacade<C extends ICheckoutDTO> {
    RecordDTO getRecord(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    C getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);

    void deleteCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);
}
