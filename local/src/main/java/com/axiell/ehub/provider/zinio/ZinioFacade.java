package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import java.util.List;

public class ZinioFacade implements IZinioFacade {


    @Override
    public PatronDTO getOrCreatePatron(ContentProviderConsumer contentProviderConsumer, Patron patron) {
        return null;
    }

    @Override
    public CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId) {
        return null;
    }

    @Override
    public List<CheckoutDTO> getCheckouts(ContentProviderConsumer contentProviderConsumer) {
        return null;
    }
}
