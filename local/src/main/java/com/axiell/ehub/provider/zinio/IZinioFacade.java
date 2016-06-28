package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import java.util.List;

interface IZinioFacade {

    PatronDTO getOrCreatePatron(ContentProviderConsumer contentProviderConsumer, Patron patron);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId);

    List<CheckoutDTO> getCheckouts(ContentProviderConsumer contentProviderConsumer);
}
