package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.auth.Patron;

import java.util.List;

interface IOcdFacade {

    List<MediaDTO> getAllMedia(ContentProviderConsumer contentProviderConsumer);

    List<PatronDTO> getAllPatrons(ContentProviderConsumer contentProviderConsumer);

    String getOrCreatePatron(ContentProviderConsumer contentProviderConsumer, Patron patron);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, String patronId, String contentProviderRecordId);

    void checkin(ContentProviderConsumer contentProviderConsumer, String patronId, String contentProviderRecordId);

    CheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, String patronId, String transactionId);

    List<CheckoutDTO> getCheckouts(ContentProviderConsumer contentProviderConsumer, String patronId);
}
