package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ResponseAnalyser;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class OcdAuthenticator implements IOcdAuthenticator {

    @Autowired
    private IOcdFacade ocdFacade;

    @Override
    public BearerToken authenticate(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        BearerToken bearerToken;
        try {
            bearerToken = ocdFacade.newBearerToken(contentProviderConsumer, patron);
        } catch (ClientResponseFailure crf) {
            ResponseAnalyser responseAnalyser = ResponseAnalyser.from(crf);
            if (responseAnalyser.isUnauthorized()) {
                ocdFacade.addPatron(contentProviderConsumer, patron);
                bearerToken = ocdFacade.newBearerToken(contentProviderConsumer, patron);
            } else
                throw crf;
        }
        return bearerToken;
    }
}
