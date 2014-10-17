package com.axiell.ehub.lms.palma;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.logging.LoggingHandler;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
            addLoggingHandler(palmaPort);
            palmaPorts.put(wsdlUrl, palmaPort);
        }
        return palmaPort;
    }

    protected abstract PalmaWsdlUrl makePalmaWsdlUrl(EhubConsumer ehubConsumer);

    protected abstract P makePalmaPort(URL wsdlUrl);

    private void addLoggingHandler(final P palmaPort) {
        BindingProvider bp = (BindingProvider) palmaPort;
        Binding binding = bp.getBinding();
        @SuppressWarnings("rawtypes")
        List<Handler> handlerList = binding.getHandlerChain();
        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }
        LoggingHandler loggingHandler = new LoggingHandler();
        handlerList.add(loggingHandler);
        binding.setHandlerChain(handlerList);
    }
}
