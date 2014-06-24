package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;
import org.jboss.resteasy.client.ClientResponse;

public class OverdriveErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(ClientResponse<?> response) {
        final ErrorDetails errorDetails = response.getEntity(ErrorDetails.class);
        return errorDetails == null ? null : errorDetails.getMessage();
    }
}
