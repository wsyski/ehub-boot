package com.axiell.ehub.provider.generic;

import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;
import com.axiell.ehub.provider.borrowbox.ErrorResponseDTO;

import javax.ws.rs.core.Response;

public class ProviderErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(Response response) {
        final com.axiell.ehub.provider.borrowbox.ErrorResponseDTO errorResponse = response.readEntity(ErrorResponseDTO.class);
        return errorResponse == null ? null : errorResponse.getMessage();
    }
}
