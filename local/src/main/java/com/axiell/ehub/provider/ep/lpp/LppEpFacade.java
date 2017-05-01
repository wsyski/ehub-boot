package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.auth.Patron;
import com.axiell.ehub.provider.ep.AbstractEpFacade;
import org.springframework.stereotype.Component;

@Component
public class LppEpFacade extends AbstractEpFacade<LppCheckoutDTO, LppCheckoutRequestDTO, ILppEpResource> implements ILppEpFacade {

    @Override
    public LppCheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final ILppEpResource epResource = getEpResource(contentProviderConsumer, patron);
        final LppCheckoutRequestDTO checkoutRequest = new LppCheckoutRequestDTO(contentProviderRecordId);
        return epResource.checkout(checkoutRequest);
    }

    @Override
    protected Class<ILppEpResource> getIEpResourceClass() {
        return ILppEpResource.class;
    }
}





