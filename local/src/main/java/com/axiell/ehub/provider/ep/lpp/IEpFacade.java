package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.RecordDTO;

public interface IEpFacade {

    RecordDTO getRecord(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    CheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);
}
