package com.axiell.ehub.lms.palma;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.logging.ISoapLoggingHandlerAppender;

import javax.xml.ws.BindingProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey.ARENA_LOCAL_API_ENDPOINT;

abstract class AbstractPalmaPortFactory<P> {
    private volatile ConcurrentMap<String, P> palmaPorts = new ConcurrentHashMap<>();

    public P getInstance(final EhubConsumer ehubConsumer) {
        PalmaWsdlUrl palmaWsdlUrl = makePalmaWsdlUrl(ehubConsumer);
        String palmaWsdlUrlAsString = palmaWsdlUrl.asString();
        P palmaPort = palmaPorts.get(palmaWsdlUrlAsString);
        if (palmaPort == null) {
            palmaPort = makePalmaPort(palmaWsdlUrl.asURL());
            BindingProvider bindingProvider = (BindingProvider) palmaPort;
            URI endpointUri = makeEndpointUri(ehubConsumer, bindingProvider);
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUri.toString());
            getSoapLoggingHandlerAppender().addLoggingHandler(palmaPort);
            palmaPorts.put(palmaWsdlUrlAsString, palmaPort);
        }
        return palmaPort;
    }

    private static URI makeEndpointUri(final EhubConsumer ehubConsumer, final BindingProvider bindingProvider) {
        URI arenaLocalApiEndpointUri;
        try {
            arenaLocalApiEndpointUri = new URI(ehubConsumer.getProperties().get(ARENA_LOCAL_API_ENDPOINT));
            // palmaUri = new URI("http://localhost:16520/arena.pa.palma");
        } catch (URISyntaxException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
        try {
            URI endpointUri = new URI((String) bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
            return new URI(arenaLocalApiEndpointUri.getScheme(), null, arenaLocalApiEndpointUri.getHost(), arenaLocalApiEndpointUri.getPort(), endpointUri.getPath(), endpointUri.getQuery(), endpointUri.getFragment());
        } catch (URISyntaxException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }

    }

    protected abstract PalmaWsdlUrl makePalmaWsdlUrl(EhubConsumer ehubConsumer);

    protected abstract P makePalmaPort(URL wsdlUrl);

    protected abstract ISoapLoggingHandlerAppender getSoapLoggingHandlerAppender();
}
