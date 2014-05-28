package com.axiell.ehub.logging;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class, TimeLoggingExecutionInterceptor.class, Status.class})
public class TimeLoggingExecutionInterceptorTest {
    private static final String GET = "GET";
    private static final String URL = "http://arena.com/some/request";
    private static final int STATUS_OK = 200;
    private static final long ELAPSE = 1L;
    private static final int STATUS_SERVER_ERROR = 500;
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SEPARATOR = "; ";
    private static final String INVOCATION_TRAIL_NOT_AVAILABLE = "INVOCATION_TRAIL_NOT_AVAILABLE";
    @Mock
    private Logger logger;

    @Mock
    private ClientExecutionContext ctx;
    @Mock
    private ClientResponse clientResponse;
    @Mock
    private Status responseStatus;
    @Mock
    private ClientRequest clientRequest;
    @Mock
    private StopWatch stopWatch;

    private TimeLoggingExecutionInterceptor underTest;

    @Before
    public void setUp() throws Exception {
        mockStatic(Logger.class);
        mockStatic(Status.class);
        whenNew(StopWatch.class).withNoArguments().thenReturn(stopWatch);
        given(Logger.getLogger("time")).willReturn(logger);
        underTest = new TimeLoggingExecutionInterceptor();
    }

    @Test
    public void logSuccessfulHttpInvocation() throws Exception {
        givenThereIsRequestData();
        givenRequestIsOk();
        whenInterceptorIsTriggered();
        thenLogStatementIsTheExpectedOk();
    }

    @Test
    public void logUnsuccessfulHttpInvocation() throws Exception {
        givenThereIsRequestData();
        givenRequestIsNotOk();
        whenInterceptorIsTriggered();
        thenLogStatementIsTheExpectedNotOk();
    }

    @Test
    public void logExceptionHttpInvocation() throws Exception {
        givenThereIsRequestDataWithoutResponse();
        givenExceptionIsThrown();
        try {
            whenInterceptorIsTriggered();
            fail("Should have thrown exception");
        } catch (Exception expected) {
        }
        thenLogStatementIsTheExpectedException();
    }

    private void givenExceptionIsThrown() throws Exception {
        given(ctx.proceed()).willThrow(new RuntimeException(ERROR_MESSAGE));
    }

    private void thenLogStatementIsTheExpectedOk() {
        verify(logger).info(INVOCATION_TRAIL_NOT_AVAILABLE + SEPARATOR + GET + SEPARATOR + URL + SEPARATOR + ELAPSE + SEPARATOR + STATUS_OK);
    }

    private void thenLogStatementIsTheExpectedNotOk() {
        verify(logger).info(INVOCATION_TRAIL_NOT_AVAILABLE + SEPARATOR + GET + SEPARATOR + URL + SEPARATOR + ELAPSE + SEPARATOR + STATUS_SERVER_ERROR);
    }

    private void thenLogStatementIsTheExpectedException() {
        verify(logger).info(INVOCATION_TRAIL_NOT_AVAILABLE + SEPARATOR + GET + SEPARATOR + URL + SEPARATOR + ELAPSE + SEPARATOR + RuntimeException.class.getName() + ": " + ERROR_MESSAGE);
    }

    private void whenInterceptorIsTriggered() throws Exception {
        underTest.execute(ctx);
    }

    private void givenThereIsRequestData() throws Exception {
        givenThereIsRequestDataWithoutResponse();
        given(clientResponse.getResponseStatus()).willReturn(responseStatus);
        given(ctx.proceed()).willReturn(clientResponse);
    }

    private void givenThereIsRequestDataWithoutResponse() throws Exception {
        given(ctx.getRequest()).willReturn(clientRequest);
        given(clientRequest.getHttpMethod()).willReturn(GET);
        given(clientRequest.getUri()).willReturn(URL);
        given(stopWatch.getTime()).willReturn(ELAPSE);
    }

    private void givenRequestIsOk() throws Exception {
        given(responseStatus.getStatusCode()).willReturn(STATUS_OK);
        given(clientResponse.getStatus()).willReturn(STATUS_OK);
    }

    private void givenRequestIsNotOk() throws Exception {
        given(responseStatus.getStatusCode()).willReturn(STATUS_SERVER_ERROR);
        given(clientResponse.getStatus()).willReturn(STATUS_SERVER_ERROR);
    }
}
