package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;
import javax.ws.rs.core.Response;

public class OcdErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(Response response) {
        final ErrorDetailsDTO errorDetails = response.readEntity(ErrorDetailsDTO.class);
        return errorDetails == null ? null : errorDetails.getMessage();
    }
}
