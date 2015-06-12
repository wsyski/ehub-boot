package com.axiell.ehub.provider;

import org.jboss.resteasy.client.ClientResponse;

public interface ILegacyContentProviderErrorResponseBodyReader {

    String read(ClientResponse<?> response);
}
