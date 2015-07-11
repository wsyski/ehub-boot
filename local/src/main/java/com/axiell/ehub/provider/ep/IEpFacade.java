package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

interface IEpFacade {

    RecordDTO getRecord(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId, String formatId);

    CheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);
}
