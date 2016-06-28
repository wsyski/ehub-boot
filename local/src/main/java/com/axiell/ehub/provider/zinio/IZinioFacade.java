package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ocd.BearerToken;
import com.axiell.ehub.provider.ocd.CheckoutDTO;
import com.axiell.ehub.provider.ocd.MediaDTO;
import com.axiell.ehub.provider.ocd.PatronDTO;

import java.util.List;

interface IZinioFacade {

    PatronDTO addPatron(ContentProviderConsumer contentProviderConsumer, Patron patron);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, BearerToken bearerToken, String contentProviderRecordId);

    List<CheckoutDTO> getCheckouts(ContentProviderConsumer contentProviderConsumer, BearerToken bearerToken);
}
