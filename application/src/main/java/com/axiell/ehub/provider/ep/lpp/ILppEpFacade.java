package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.ep.IEpFacade;

public interface ILppEpFacade extends IEpFacade<LppCheckoutDTO> {
    LppCheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);
}
