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
    public void addLoggingHandler(Object soapService) {
        BindingProvider bp = (BindingProvider) soapService;
        Binding binding = bp.getBinding();
        @SuppressWarnings("rawtypes")
        List<Handler> handlerList = binding.getHandlerChain();
        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }
        SoapLoggingHandler loggingHandler = new SoapLoggingHandler();
        handlerList.add(loggingHandler);
        binding.setHandlerChain(handlerList);
    }
}
