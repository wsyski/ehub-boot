package com.axiell.ehub.lms.palma;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.logging.ISoapLoggingHandlerAppender;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class AbstractPalmaPortFactory<P> {
    private volatile ConcurrentMap<String, P> palmaPorts = new ConcurrentHashMap<>();

    public P getInstance(final EhubConsumer ehubConsumer) {
        PalmaWsdlUrl palmaWsdlUrl = makePalmaWsdlUrl(ehubConsumer);
        String wsdlUrl = palmaWsdlUrl.asString();
        P palmaPort = palmaPorts.get(wsdlUrl);
        if (palmaPort == null) {
            palmaPort = makePalmaPort(palmaWsdlUrl.asURL());
            getSoapLoggingHandlerAppender().addLoggingHandler(palmaPort);
            palmaPorts.put(wsdlUrl, palmaPort);
        }
        return palmaPort;
    }

    protected abstract PalmaWsdlUrl makePalmaWsdlUrl(EhubConsumer ehubConsumer);

    protected abstract P makePalmaPort(URL wsdlUrl);

    protected abstract ISoapLoggingHandlerAppender getSoapLoggingHandlerAppender();
}
