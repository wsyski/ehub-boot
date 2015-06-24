package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;
import javax.ws.rs.core.Response;

public class OcdErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(Response response) {
        final ErrorDTO errorResponse = response.readEntity(ErrorDTO.class);
        return errorResponse == null ? null : errorResponse.getMessage();
    }
}
