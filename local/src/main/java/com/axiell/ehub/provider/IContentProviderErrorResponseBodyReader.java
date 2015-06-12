package com.axiell.ehub.provider;

import javax.ws.rs.core.Response;

public interface IContentProviderErrorResponseBodyReader {

    String read(Response response);
}
