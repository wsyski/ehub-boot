package com.axiell.ehub.logging;

import org.springframework.stereotype.Component;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;

@Component
public class SoapLoggingHandlerAppender implements ISoapLoggingHandlerAppender {

    @Override
    public void addLoggingHandler(final Object soapService) {
        BindingProvider bp = (BindingProvider) soapService;
        Binding binding = bp.getBinding();
        @SuppressWarnings("rawtypes")
        List<Handler> handlerList = binding.getHandlerChain();
        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }
        SoapLoggingHandler soapLoggingHandler = new SoapLoggingHandler();
        handlerList.add(soapLoggingHandler);
        binding.setHandlerChain(handlerList);
    }
}
