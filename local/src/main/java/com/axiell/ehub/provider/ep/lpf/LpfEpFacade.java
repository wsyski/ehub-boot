package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.EpResourceFactory;
import com.axiell.ehub.provider.ep.RecordDTO;
import org.springframework.stereotype.Component;

@Component
public class LpfEpFacade implements ILpfEpFacade {
    private EpResourceFactory<ILpfEpResource> epResourceFactory=new EpResourceFactory<>();
    @Override
    public RecordDTO getRecord(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final ILpfEpResource epResource = epResourceFactory.create(ILpfEpResource.class, contentProviderConsumer, patron);
        return epResource.getRecord(contentProviderRecordId);
    }

    @Override
    public LpfCheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId,
                                   final String formatId) {
        final ILpfEpResource epResource = epResourceFactory.create(ILpfEpResource.class, contentProviderConsumer, patron);
        final LpfCheckoutRequestDTO checkoutRequest = new LpfCheckoutRequestDTO(contentProviderRecordId, formatId);
        return epResource.checkout(checkoutRequest);
    }

    @Override
    public LpfCheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron,
                                      final String contentProviderLoanId) {
        final ILpfEpResource epResource = epResourceFactory.create(ILpfEpResource.class, contentProviderConsumer, patron);
        return epResource.getCheckout(contentProviderLoanId);
    }
}
