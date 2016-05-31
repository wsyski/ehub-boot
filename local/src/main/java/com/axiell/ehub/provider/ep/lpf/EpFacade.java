package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.EpResourceFactory;
import com.axiell.ehub.provider.ep.RecordDTO;
import org.springframework.stereotype.Component;

@Component("epLpfFacade")
public class EpFacade implements IEpFacade {
    private EpResourceFactory<IEpResource> epResourceFactory=new EpResourceFactory<>();
    @Override
    public RecordDTO getRecord(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final IEpResource epResource = epResourceFactory.create(IEpResource.class, contentProviderConsumer, patron);
        return epResource.getRecord(contentProviderRecordId);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId,
                                final String formatId) {
        final IEpResource epResource = epResourceFactory.create(IEpResource.class, contentProviderConsumer, patron);
        final CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(contentProviderRecordId, formatId);
        return epResource.checkout(checkoutRequest);
    }

    @Override
    public CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron,
                                   final String contentProviderLoanId) {
        final IEpResource epResource = epResourceFactory.create(IEpResource.class, contentProviderConsumer, patron);
        return epResource.getCheckout(contentProviderLoanId);
    }
}
