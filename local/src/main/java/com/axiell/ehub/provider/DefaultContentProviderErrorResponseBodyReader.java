package com.axiell.ehub.provider;

import org.jboss.resteasy.client.ClientResponse;

class DefaultContentProviderErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(final ClientResponse<?> response) {
        return response.getEntity(String.class);
    }
}
