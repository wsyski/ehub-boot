package com.axiell.ehub.provider.epi;

import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;

import javax.ws.rs.core.Response;

public class ProviderErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(Response response) {
        final ErrorDTO providerError = response.readEntity(ErrorDTO.class);
        return providerError == null ? null : providerError.getMessage();
    }
}
