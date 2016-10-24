package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import java.util.List;

interface IOcdFacade {

    List<MediaDTO> getAllMedia(ContentProviderConsumer contentProviderConsumer);

    PatronDTO addPatron(ContentProviderConsumer contentProviderConsumer, Patron patron);

    List<PatronDTO> getAllPatrons(ContentProviderConsumer contentProviderConsumer);

    PatronDTO getPatronByEmail(ContentProviderConsumer contentProviderConsumer, String email);

    BearerToken newBearerToken(ContentProviderConsumer contentProviderConsumer, Patron patron);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, BearerToken bearerToken, String contentProviderRecordId);

    List<CheckoutDTO> getCheckouts(ContentProviderConsumer contentProviderConsumer, BearerToken bearerToken);
}
