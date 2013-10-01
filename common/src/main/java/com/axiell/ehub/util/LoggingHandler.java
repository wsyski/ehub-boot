package com.axiell.ehub.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;

import static com.axiell.ehub.logging.ToString.lineFeed;
import static com.axiell.ehub.logging.ToString.soapMessageToString;

/**
 * SOAP Logging Handler
 */
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);

    public boolean handleMessage(final SOAPMessageContext soapMessageContext) {
        if (LOGGER.isDebugEnabled()) {
            doHandleMessage(soapMessageContext);
        }
        return true;
    }

    private void doHandleMessage(final SOAPMessageContext soapMessageContext) {
        boolean isRequest = isMessageContextARequest(soapMessageContext);
        SOAPMessage soapMessage = soapMessageContext.getMessage();
        LOGGER.debug(writeLogHeading(isRequest) + lineFeed() + soapMessageToString(soapMessage));
    }

    private String writeLogHeading(final boolean request) {
        return (request ? "SOAP request:" : "SOAP response:");
    }

    private Boolean isMessageContextARequest(final SOAPMessageContext soapMessageContext) {
        return (Boolean) soapMessageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
    }

    public boolean handleFault(final SOAPMessageContext soapMessageContext) {
        SOAPMessage soapMessage = soapMessageContext.getMessage();
        LOGGER.error("SOAP response:" + lineFeed() + soapMessageToString(soapMessage));
        return true;
    }

    @Override
    public void close(MessageContext c) {
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }
}
