package com.axiell.ehub.common.controller.provider.filter;


import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import java.util.Locale;

@ConstrainedTo(RuntimeType.CLIENT)
@Provider
public class AcceptLanguageClientRequestFilter implements ClientRequestFilter {

    private Locale locale;

    public AcceptLanguageClientRequestFilter(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, locale.toLanguageTag());
    }
}
