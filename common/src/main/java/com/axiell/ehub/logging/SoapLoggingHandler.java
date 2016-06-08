package com.axiell.ehub.logging;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.util.Set;


public class SoapLoggingHandler implements SOAPHandler<SOAPMessageContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoapLoggingHandler.class);
    private static final String KEY_STOP_WATCH = SoapLoggingHandler.class.getName() + ".stopWatch";

    @Override
    public boolean handleMessage(final SOAPMessageContext soapMessageContext) {
        if (Boolean.TRUE.equals(soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {
            handleRequest(soapMessageContext);
        } else {
            handleResponse(soapMessageContext);
        }
        return true;
    }

    private void handleRequest(final SOAPMessageContext soapMessageContext) {
        if (LOGGER.isDebugEnabled()) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            soapMessageContext.put(KEY_STOP_WATCH, stopWatch);
            final String logMessage = makeRequestLogMessage(soapMessageContext);
            LOGGER.debug(logMessage);
        }
    }

    private void handleResponse(final SOAPMessageContext soapMessageContext) {
        if (LOGGER.isDebugEnabled()) {
            StopWatch stopWatch = StopWatch.class.cast(soapMessageContext.get(KEY_STOP_WATCH));
            final String logMessage = makeResponseLogMessage(soapMessageContext, stopWatch);
            LOGGER.debug(logMessage);
        }
    }

    private String makeRequestLogMessage(final SOAPMessageContext soapMessageContext) {
        String prefix = "SOAP request {" + SystemUtils.LINE_SEPARATOR;
        return prefix + url(soapMessageContext) + body(soapMessageContext) + suffix();
    }

    private String makeResponseLogMessage(final SOAPMessageContext soapMessageContext, final StopWatch stopWatch) {
        StringBuilder prefix = new StringBuilder("SOAP response");
        if (stopWatch != null) {
            prefix.append(" time: ");
            prefix.append(stopWatch.getTime());
        }
        prefix.append(" {");
        prefix.append(SystemUtils.LINE_SEPARATOR);
        return prefix.toString() + url(soapMessageContext) + body(soapMessageContext) + suffix();
    }

    private String url(SOAPMessageContext soapMessageContext) {
        Object url = soapMessageContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        return "URL: " + SystemUtils.LINE_SEPARATOR + url + SystemUtils.LINE_SEPARATOR;
    }

    private String body(SOAPMessageContext soapMessageContext) {
        final SOAPMessage soapMessage = soapMessageContext.getMessage();
        String body = soapMessageToString(soapMessage);
        return "Body: " + SystemUtils.LINE_SEPARATOR + body + SystemUtils.LINE_SEPARATOR;
    }

    private String suffix() {
        return "}";
    }

    @Override
    public boolean handleFault(final SOAPMessageContext soapMessageContext) {
        StopWatch stopWatch = StopWatch.class.cast(soapMessageContext.get(KEY_STOP_WATCH));
        final String logMessage = makeResponseLogMessage(soapMessageContext, stopWatch);
        LOGGER.error(logMessage);
        return true;
    }

    public static String soapMessageToString(final SOAPMessage soapMessageToProcess) {
        StringBuilder soapMessageStringBuilder = new StringBuilder();
        StringBuilderOutputStream soapMessageStream = new StringBuilderOutputStream();
        appendSoapMessage(soapMessageToProcess, soapMessageStringBuilder, soapMessageStream);
        return soapMessageStringBuilder.toString();
    }

    private static void appendSoapMessage(final SOAPMessage soapMessageToProcess, final StringBuilder soapMessageStringBuilder, final StringBuilderOutputStream soapMessageStream) {
        readSoapMessage(soapMessageToProcess, soapMessageStream);
        soapMessageStringBuilder.append(soapMessageStream.toString());
    }

    private static void readSoapMessage(final SOAPMessage soapMessageToProcess, final StringBuilderOutputStream soapMessageStream) {
        try {
            soapMessageToProcess.writeTo(soapMessageStream);
        } catch (SOAPException | IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
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
