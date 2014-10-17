package com.axiell.ehub.util;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.UnauthorizedException;
import org.jboss.resteasy.spi.BadRequestException;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.Assert.assertTrue;

public class RestEasyBadRequestExceptionMapperTest {

    @Test
    public void testToResponse() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(out);
        PrintStream old = System.out;
        setOut(printStream);
        BadRequestException ex = new BadRequestException("Got a silly nullpointer somewhere", new NullPointerException("NULLPOINTER!!! AAHHH!!!"));
        RestEasyBadRequestExceptionMapper mapper = new RestEasyBadRequestExceptionMapper();
        final Response response = mapper.toResponse(ex);
        Assert.assertNotNull(response);
        setOut(old);
        assertTrue(out.toString("UTF-8").contains("Got a silly nullpointer somewhere"));
        assertTrue(out.toString("UTF-8").contains("NULLPOINTER!!! AAHHH!!!"));
        assertTrue(out.toString("UTF-8").contains("java.lang.NullPointerException"));

        BadRequestException exc = new BadRequestException("Got a silly UnautorizedException somewhere", new UnauthorizedException(
                ErrorCause.MISSING_AUTHORIZATION_HEADER));
        final Response unauthorized = mapper.toResponse(exc);
        Assert.assertNotNull(unauthorized);
    }

    private void setOut(final PrintStream out) {
        System.setOut(out);
        System.setErr(out);
    }
}
