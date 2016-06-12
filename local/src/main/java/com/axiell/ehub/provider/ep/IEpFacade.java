package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

public interface IEpFacade<C extends ICheckoutDTO> {
    RecordDTO getRecord(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    C getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);

    void deleteCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);
}
