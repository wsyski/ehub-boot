package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OCD_BASIC_TOKEN;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OCD_LIBRARY_ID;

@Component
class OcdFacade implements IOcdFacade {

    @Override
    public List<MediaDTO> getAllMedia(ContentProviderConsumer contentProviderConsumer) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.getAllMedia(basicToken, libraryId);
    }

    @Override
    public PatronDTO addPatron(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final PatronDTO patronDTO = new PatronDTO(patron);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.addPatron(basicToken, libraryId, patronDTO);
    }

    @Override
    public BearerToken newBearerToken(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final PatronTokenDTO patronTokenDTO = new PatronTokenDTO(contentProviderConsumer, patron);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.newBearerToken(basicToken, patronTokenDTO);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final BearerToken bearerToken, final String contentProviderRecordId) {
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.checkout(bearerToken, "hypermedia", contentProviderRecordId);
    }

    @Override
    public List<CheckoutDTO> getCheckouts(final ContentProviderConsumer contentProviderConsumer, final BearerToken bearerToken) {
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.getCheckouts(bearerToken);
    }

}
