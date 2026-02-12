package com.axiell.ehub.local.provider.ep.lpp;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.local.provider.ep.IEpFacade;

public interface ILppEpFacade extends IEpFacade<LppCheckoutDTO> {
    LppCheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);
}
