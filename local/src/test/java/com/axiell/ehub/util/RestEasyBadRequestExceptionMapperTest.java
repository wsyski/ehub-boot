package com.axiell.ehub.util;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.UnauthorizedException;
import org.jboss.resteasy.spi.BadRequestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RestEasyBadRequestExceptionMapperTest {

    private RestEasyBadRequestExceptionMapper underTest;

    @Mock
    private HttpHeaders headers;

    @Before
    public void setUp() {
        underTest = new RestEasyBadRequestExceptionMapper();
        underTest.setHeaders(headers);
        given(headers.getMediaType()).willReturn(MediaType.APPLICATION_XML_TYPE);
    }

    @Test
    public void xmlNullPointerException() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(out);
        PrintStream old = System.out;
        setOut(printStream);
        BadRequestException ex = new BadRequestException("Got a silly nullpointer somewhere", new NullPointerException("NULLPOINTER!!! AAHHH!!!"));
        final Response response = underTest.toResponse(ex);
        Assert.assertNotNull(response);
        setOut(old);
        assertTrue(out.toString("UTF-8").contains("Got a silly nullpointer somewhere"));
        assertTrue(out.toString("UTF-8").contains("NULLPOINTER!!! AAHHH!!!"));
        assertTrue(out.toString("UTF-8").contains("java.lang.NullPointerException"));
    }

    @Test
    public void xmlUnauthorizedException() throws Exception {
        BadRequestException ex = new BadRequestException("Got a silly UnautorizedException somewhere", new UnauthorizedException(
                ErrorCause.MISSING_AUTHORIZATION_HEADER));
        final Response unauthorized = underTest.toResponse(ex);
        Assert.assertNotNull(unauthorized);
    }

    private void setOut(final PrintStream out) {
        System.setOut(out);
        System.setErr(out);
    }
}
