package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.IEpFacade;

public interface ILppEpFacade extends IEpFacade {
    LppCheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);

    LppCheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderLoanId);
}
