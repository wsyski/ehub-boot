package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;
import org.jboss.resteasy.client.ClientResponse;

public class Elib3ErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(final ClientResponse<?> response) {
        final ErrorResponse errorResponse = response.getEntity(ErrorResponse.class);
        return errorResponse == null ? null : errorResponse.getReason();
    }
}
