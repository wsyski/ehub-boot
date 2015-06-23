package com.axiell.ehub.provider.generic;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;
import com.axiell.ehub.provider.borrowbox.ErrorResponseDTO;

import javax.ws.rs.core.Response;

public class ProviderErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(Response response) {
        final ProviderErrorDTO providerError = response.readEntity(ProviderErrorDTO.class);
        return providerError == null ? null : providerError.getMessage();
    }
}
