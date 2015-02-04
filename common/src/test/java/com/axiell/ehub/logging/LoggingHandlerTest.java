package com.axiell.ehub.logging;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import java.util.Set;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;

@Ignore
@RunWith(PowerMockRunner.class)
public class LoggingHandlerTest {
    private boolean actualResult;
    private Set<QName> actualHeaders;

    @Mock
    private SOAPMessageContext messageContext;

    @Mock
    private Logger logger;

    @Mock
    private SOAPMessage soapMessage;


    @Before
    public void init() {
        mockStatic(LoggerFactory.class);
        mockStatic(ToStringConverter.class);
        given(LoggerFactory.getLogger(any(Class.class))).willReturn(logger);
        given(messageContext.getMessage()).willReturn(soapMessage);
        given(ToStringConverter.soapMessageToString(soapMessage)).willReturn("SoapMessageToString");
        given(ToStringConverter.lineFeed()).willReturn("\n");

    }

    @PrepareForTest({SoapLoggingHandler.class, LoggerFactory.class, ToStringConverter.class})
    @Test
    public void handleResponseMessageWithDebugDisabled() {
        givenDebugLoggingIsDisabled();
        givenMessageIsAResponse();
        whenMessageIsHandled();
        thenNothingIsWrittenToLog();
        thenResultOfMethodIsTrue();
    }

    @PrepareForTest({SoapLoggingHandler.class, LoggerFactory.class, ToStringConverter.class})
    @Test
    public void handleRequestMessageWithDebugDisabled() {
        givenDebugLoggingIsDisabled();
        givenMessageIsARequest();
        whenMessageIsHandled();
        thenNothingIsWrittenToLog();
        thenResultOfMethodIsTrue();
    }

    @PrepareForTest({SoapLoggingHandler.class, LoggerFactory.class, ToStringConverter.class})
    @Test
    public void handleResponseMessageWithDebugEnabled() {
        givenDebugLoggingIsEnabled();
        givenMessageIsAResponse();
        whenMessageIsHandled();
        thenResponseMessageIsWrittenToLog();
        thenResultOfMethodIsTrue();
    }

    @PrepareForTest({SoapLoggingHandler.class, LoggerFactory.class, ToStringConverter.class})
    @Test
    public void handleRequestMessageWithDebugEnabled() {
        givenDebugLoggingIsEnabled();
        givenMessageIsARequest();
        whenMessageIsHandled();
        thenRequestMessageIsWrittenToLog();
        thenResultOfMethodIsTrue();
    }

    @PrepareForTest({SoapLoggingHandler.class, LoggerFactory.class, ToStringConverter.class})
    @Test
    public void handleFault() {
        whenfaultIsHandled();
        thenResponseMessageIsWrittenToErrorLog();
        thenResultOfMethodIsTrue();
    }

    @PrepareForTest({SoapLoggingHandler.class, LoggerFactory.class, ToStringConverter.class})
    @Test
    public void getHeaders() {
        whenHeadersAreRequested();
        headersAreAlwaysNull();
    }

    private void headersAreAlwaysNull() {
        assertNull(actualHeaders);
    }

    private void whenHeadersAreRequested() {
        actualHeaders = new SoapLoggingHandler().getHeaders();
    }

    private void thenResultOfMethodIsTrue() {
        assertTrue(actualResult);
    }


    private void thenRequestMessageIsWrittenToLog() {
        verify(logger).debug("SOAP request:" + "\nSoapMessageToString");
        verify(logger).isDebugEnabled();
        verifyNoMoreInteractions(logger);
    }

    private void thenResponseMessageIsWrittenToLog() {
        verify(logger).debug("SOAP response:" + "\nSoapMessageToString");
        verify(logger).isDebugEnabled();
        verifyNoMoreInteractions(logger);
    }

    private void thenResponseMessageIsWrittenToErrorLog() {
        verify(logger).error("SOAP response:" + "\nSoapMessageToString");
        verifyNoMoreInteractions(logger);
    }

    private void thenNothingIsWrittenToLog() {
        verify(logger).isDebugEnabled();
        verifyNoMoreInteractions(logger);
    }

    private void whenMessageIsHandled() {
        actualResult = new SoapLoggingHandler().handleMessage(messageContext);
    }

    private void whenfaultIsHandled() {
        actualResult = new SoapLoggingHandler().handleFault(messageContext);
    }

    private void givenMessageIsARequest() {
        given(messageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).willReturn(true);
    }

    private void givenMessageIsAResponse() {
        given(messageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).willReturn(false);
    }

    private void givenDebugLoggingIsEnabled() {
        given(logger.isDebugEnabled()).willReturn(true);
    }

    private void givenDebugLoggingIsDisabled() {
        given(logger.isDebugEnabled()).willReturn(false);
    }
}
