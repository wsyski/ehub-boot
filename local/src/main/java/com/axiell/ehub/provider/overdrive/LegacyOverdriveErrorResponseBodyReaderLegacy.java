package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.ILegacyContentProviderErrorResponseBodyReader;
import org.jboss.resteasy.client.ClientResponse;

public class LegacyOverdriveErrorResponseBodyReaderLegacy implements ILegacyContentProviderErrorResponseBodyReader {

    @Override
    public String read(ClientResponse<?> response) {
        final ErrorDetails errorDetails = response.getEntity(ErrorDetails.class);
        return errorDetails == null ? null : errorDetails.getMessage();
    }
}
