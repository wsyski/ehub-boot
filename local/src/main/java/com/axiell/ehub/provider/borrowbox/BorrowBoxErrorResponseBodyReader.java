package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.provider.IContentProviderErrorResponseBodyReader;

import javax.ws.rs.core.Response;

public class BorrowBoxErrorResponseBodyReader implements IContentProviderErrorResponseBodyReader {

    @Override
    public String read(Response response) {
        final ErrorResponseDTO errorResponse = response.readEntity(ErrorResponseDTO.class);
        return errorResponse == null ? null : errorResponse.getMessage();
    }
}
