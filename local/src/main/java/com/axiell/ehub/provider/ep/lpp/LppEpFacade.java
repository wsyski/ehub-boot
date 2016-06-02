package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.EpResourceFactory;
import com.axiell.ehub.provider.ep.RecordDTO;
import org.springframework.stereotype.Component;

@Component
public class LppEpFacade implements ILppEpFacade {

    private EpResourceFactory<ILppEpResource> epResourceFactory=new EpResourceFactory<>();

    @Override
    public RecordDTO getRecord(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final ILppEpResource epResource = epResourceFactory.create(ILppEpResource.class, contentProviderConsumer, patron);
        return epResource.getRecord(contentProviderRecordId);
    }

    @Override
    public LppCheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final ILppEpResource epResource = epResourceFactory.create(ILppEpResource.class, contentProviderConsumer, patron);
        final LppCheckoutRequestDTO checkoutRequest = new LppCheckoutRequestDTO(contentProviderRecordId);
        return epResource.checkout(checkoutRequest);
    }

    @Override
    public LppCheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron,
                                      final String contentProviderLoanId) {
        final ILppEpResource epResource = epResourceFactory.create(ILppEpResource.class, contentProviderConsumer, patron);
        return epResource.getCheckout(contentProviderLoanId);
    }
}
