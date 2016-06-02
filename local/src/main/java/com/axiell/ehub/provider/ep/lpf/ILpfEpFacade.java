package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.IEpFacade;

public interface ILpfEpFacade extends IEpFacade {
    LpfCheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId, String formatId);

    LpfCheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);
}
