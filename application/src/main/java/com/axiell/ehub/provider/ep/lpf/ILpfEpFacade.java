package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.ep.IEpFacade;

public interface ILpfEpFacade extends IEpFacade<LpfCheckoutDTO> {
    LpfCheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId, String formatId);
}
