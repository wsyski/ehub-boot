package com.axiell.ehub.util;

import com.axiell.ehub.EhubError;
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

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RestEasyBadRequestExceptionMapperTest {
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";

    private RestEasyBadRequestExceptionMapper underTest;

    @Mock
    private HttpHeaders headers;

    private BadRequestException badRequestException;

    @Before
    public void setUp() {
        underTest = new RestEasyBadRequestExceptionMapper();
        underTest.setHeaders(headers);
        given(headers.getMediaType()).willReturn(MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void nullPointerException() throws Exception {
        Exception originalEx = new NullPointerException();
       badRequestException = new BadRequestException(EXCEPTION_MESSAGE, originalEx);
        final Response response = underTest.toResponse(badRequestException);
        Assert.assertNotNull(response);
        Object entity = response.getEntity();
        Assert.assertEquals(entity.getClass(), EhubError.class);
        EhubError ehubError = EhubError.class.cast(entity);
        Assert.assertEquals(ehubError.getCause(), ErrorCause.BAD_REQUEST);
    }

    @Test
    public void unauthorizedException() throws Exception {
        Exception originalEx = new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER);
        badRequestException = new BadRequestException(EXCEPTION_MESSAGE, originalEx);
        final Response response = underTest.toResponse(badRequestException);
        Assert.assertNotNull(response);
        Object entity = response.getEntity();
        Assert.assertEquals(entity.getClass(), EhubError.class);
        EhubError ehubError = EhubError.class.cast(entity);
        Assert.assertEquals(ehubError.getCause(), ErrorCause.MISSING_AUTHORIZATION_HEADER);
    }
}
