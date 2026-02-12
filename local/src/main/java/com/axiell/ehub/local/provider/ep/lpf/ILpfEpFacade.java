package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.local.provider.ep.IEpFacade;

public interface ILpfEpFacade extends IEpFacade<LpfCheckoutDTO> {
    LpfCheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId, String formatId);
}
