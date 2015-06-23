package com.axiell.ehub.provider.generic;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;
import com.axiell.ehub.provider.borrowbox.ErrorResponseDTO;

import javax.ws.rs.core.Response;

public class ProviderErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(Response response) {
        final EhubError ehubError = response.readEntity(EhubError.class);
        return ehubError == null ? null : ehubError.getMessage();
    }
}
