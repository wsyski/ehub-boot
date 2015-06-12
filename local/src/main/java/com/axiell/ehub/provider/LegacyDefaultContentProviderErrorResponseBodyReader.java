package com.axiell.ehub.provider;

import org.jboss.resteasy.client.ClientResponse;

class LegacyDefaultContentProviderErrorResponseBodyReader implements ILegacyContentProviderErrorResponseBodyReader {

    @Override
    public String read(final ClientResponse<?> response) {
        return response.getEntity(String.class);
    }
}
