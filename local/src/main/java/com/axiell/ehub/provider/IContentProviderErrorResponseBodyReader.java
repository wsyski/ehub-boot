package com.axiell.ehub.provider;

import org.jboss.resteasy.client.ClientResponse;

public interface IContentProviderErrorResponseBodyReader {

    String read(ClientResponse<?> response);
}
