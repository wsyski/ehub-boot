package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.local.provider.ep.AbstractEpFacade;
import org.springframework.stereotype.Component;

@Component
public class LpfEpFacade extends AbstractEpFacade<LpfCheckoutDTO, LpfCheckoutRequestDTO, ILpfEpResource> implements ILpfEpFacade {

    @Override
    public LpfCheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId,
                                   final String formatId) {
        final ILpfEpResource epResource = getEpResource(contentProviderConsumer, patron);
        final LpfCheckoutRequestDTO checkoutRequest = new LpfCheckoutRequestDTO(contentProviderRecordId, formatId);
        return epResource.checkout(checkoutRequest);
    }

    @Override
    protected Class<ILpfEpResource> getIEpResourceClass() {
        return ILpfEpResource.class;
    }
}





