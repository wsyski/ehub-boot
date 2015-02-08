package com.axiell.ehub.util;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.InternalServerErrorException;
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
    private Response response;

    @Before
    public void setUp() {
        underTest = new RestEasyBadRequestExceptionMapper();
        underTest.setHeaders(headers);
        given(headers.getMediaType()).willReturn(MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void nullPointerException() {
        givenCreatedBadRequestException(new NullPointerException());
        whenResponseGenerated();
        thenValidResponse(ErrorCause.BAD_REQUEST);
    }

    @Test
    public void unauthorizedException() {
        givenCreatedBadRequestException(new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.MISSING_AUTHORIZATION_HEADER);
    }

    @Test
    public void internalServerErrorException() {
        givenCreatedBadRequestException(new InternalServerErrorException(ErrorCause.CONTENT_PROVIDER_ERROR));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.CONTENT_PROVIDER_ERROR);
    }

    @Test
    public void forbiddenException() {
        givenCreatedBadRequestException(new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.LMS_CHECKOUT_DENIED);
    }

    private void whenResponseGenerated() {
        response = underTest.toResponse(badRequestException);
    }

    private void givenCreatedBadRequestException(final Exception exception) {
        badRequestException = new BadRequestException(EXCEPTION_MESSAGE, exception);
    }

    private void thenValidResponse(final ErrorCause errorCause) {
        Assert.assertNotNull(response);
        Object entity = response.getEntity();
        Assert.assertEquals(entity.getClass(), EhubError.class);
        EhubError ehubError = EhubError.class.cast(entity);
        Assert.assertEquals(ehubError.getCause(), errorCause);
    }
}
