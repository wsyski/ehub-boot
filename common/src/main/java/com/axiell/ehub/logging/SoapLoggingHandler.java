package com.axiell.ehub.logging;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;

import static com.axiell.ehub.logging.ToString.lineFeed;
import static com.axiell.ehub.logging.ToString.soapMessageToString;

/**
 * SOAP Logging Handler
 */
class SoapLoggingHandler implements SOAPHandler<SOAPMessageContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoapLoggingHandler.class);

    public boolean handleMessage(final SOAPMessageContext soapMessageContext) {
        if (LOGGER.isDebugEnabled()) {
            final String logMessage = makeLogMessage(soapMessageContext);
            LOGGER.debug(logMessage);
        }
        return true;
    }

    private String makeLogMessage(final SOAPMessageContext soapMessageContext) {
        return prefix(soapMessageContext) + url(soapMessageContext) + body(soapMessageContext) + suffix();
    }

    private String prefix(final SOAPMessageContext soapMessageContext) {
        boolean request = isMessageContextARequest(soapMessageContext);
        String prefix = request ? "SOAP request" : "SOAP response";
        return prefix + "{" + lineFeed();
    }

    private Boolean isMessageContextARequest(final SOAPMessageContext soapMessageContext) {
        return (Boolean) soapMessageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
    }

    private String url(SOAPMessageContext soapMessageContext) {
        Object url = soapMessageContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        return "URL: " + lineFeed() + url + lineFeed();
    }

    private String body(SOAPMessageContext soapMessageContext) {
        final SOAPMessage soapMessage = soapMessageContext.getMessage();
        String body = soapMessageToString(soapMessage);
        return "Body: " + lineFeed() + body + lineFeed();
    }

    private String suffix() {
        return "}";
    }

    public boolean handleFault(final SOAPMessageContext soapMessageContext) {
        final String logMessage = makeLogMessage(soapMessageContext);
        LOGGER.error(logMessage);
        return true;
    }

    @Override
    public void close(MessageContext c) {
        // Do nothing because logging the close event is not necessary
    }

    @Override
    public Set<QName> getHeaders() {
        return Sets.newHashSet();
    }
}
