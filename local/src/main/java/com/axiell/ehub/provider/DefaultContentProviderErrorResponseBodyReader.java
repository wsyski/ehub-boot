package com.axiell.ehub.provider;

import javax.ws.rs.core.Response;

class DefaultContentProviderErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(final Response response) {
        return response.readEntity(String.class);
    }
}
