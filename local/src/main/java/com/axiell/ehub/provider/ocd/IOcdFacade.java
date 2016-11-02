package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import java.util.List;

interface IOcdFacade {

    List<MediaDTO> getAllMedia(ContentProviderConsumer contentProviderConsumer);

    PatronDTO addPatron(ContentProviderConsumer contentProviderConsumer, Patron patron);

    List<PatronDTO> getAllPatrons(ContentProviderConsumer contentProviderConsumer);

    PatronDTO getPatron(ContentProviderConsumer contentProviderConsumer, Patron patron);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, String patronId, String contentProviderRecordId);

    void checkin(ContentProviderConsumer contentProviderConsumer, String patronId, String contentProviderRecordId);

    CheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, String patronId, String transactionId);

    List<CheckoutDTO> getCheckouts(ContentProviderConsumer contentProviderConsumer, String patronId);
}
