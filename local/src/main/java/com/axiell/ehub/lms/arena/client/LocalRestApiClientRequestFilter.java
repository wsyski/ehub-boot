package com.axiell.ehub.lms.arena.client;


import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Locale;

public class LocalRestApiClientRequestFilter implements ClientRequestFilter {

    private Locale locale;

    public LocalRestApiClientRequestFilter(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, locale.toLanguageTag());
    }
}
