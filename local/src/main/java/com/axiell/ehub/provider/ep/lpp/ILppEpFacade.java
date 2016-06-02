package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.RecordDTO;

public interface ILppEpFacade {

    RecordDTO getRecord(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    LppCheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    LppCheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);
}
