package com.axiell.ehub.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SOAP Logging Handler
 */
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);

    public boolean handleMessage(final SOAPMessageContext soapMessageContext) {
        if (LOGGER.isDebugEnabled()) {
            boolean isRequest = (Boolean) soapMessageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
            SOAPMessage soapMessage = soapMessageContext.getMessage();
            LOGGER.debug((isRequest ? "SOAP request:" : "SOAP response:")+ToString.getLf() + ToString.fromSOAPMessage(soapMessage));
        }
        return true;
    }

    public boolean handleFault(final SOAPMessageContext soapMessageContext) {
        SOAPMessage soapMessage = soapMessageContext.getMessage();
        LOGGER.error("SOAP response:"+ToString.getLf() + ToString.fromSOAPMessage(soapMessage));
        return true;
    }

    @Override
    public void close(MessageContext c) {
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    public static class StringBufferOutputStream extends OutputStream {
        private StringBuffer textBuffer = new StringBuffer();

        /**
         *
         */
        public StringBufferOutputStream() {
            super();
        }

        /*
         * @see java.io.OutputStream#write(int)
         */
        public void write(int b) throws IOException {
            char a = (char) b;
            textBuffer.append(a);
        }

        public String toString() {
            return textBuffer.toString();
        }

        public void clear() {
            textBuffer.delete(0, textBuffer.length());
        }
    }
}
