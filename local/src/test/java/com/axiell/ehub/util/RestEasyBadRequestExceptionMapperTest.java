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

import javax.servlet.http.HttpServletRequest;
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

    @Mock
    private HttpServletRequest request;

    private BadRequestException badRequestException;
    private Response response;

    @Before
    public void setUp() {
        underTest = new RestEasyBadRequestExceptionMapper();
        underTest.setHeaders(headers);
        underTest.setRequest(request);
    }

    @Test
    public void nullPointerException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedBadRequestException(new NullPointerException());
        whenResponseGenerated();
        thenValidResponse(ErrorCause.BAD_REQUEST,MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void nullPointerExceptionXml() {
        givenMediaType(MediaType.APPLICATION_XML_TYPE);
        givenCreatedBadRequestException(new NullPointerException());
        whenResponseGenerated();
        thenValidResponse(ErrorCause.BAD_REQUEST,MediaType.APPLICATION_XML_TYPE);
    }

    @Test
    public void nullPointerExceptionV1() {
        givenMediaType(null);
        givenRequestUri("/v1/formats");
        givenCreatedBadRequestException(new NullPointerException());
        whenResponseGenerated();
        thenValidResponse(ErrorCause.BAD_REQUEST,MediaType.APPLICATION_XML_TYPE);
    }

    @Test
    public void nullPointerExceptionV2() {
        givenMediaType(null);
        givenRequestUri("/v2/formats");
        givenCreatedBadRequestException(new NullPointerException());
        whenResponseGenerated();
        thenValidResponse(ErrorCause.BAD_REQUEST,MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void unauthorizedException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedBadRequestException(new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.MISSING_AUTHORIZATION_HEADER,MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void internalServerErrorException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedBadRequestException(new InternalServerErrorException(ErrorCause.CONTENT_PROVIDER_ERROR));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.CONTENT_PROVIDER_ERROR,MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void forbiddenException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedBadRequestException(new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.LMS_CHECKOUT_DENIED,MediaType.APPLICATION_JSON_TYPE);
    }

    private void whenResponseGenerated() {
        response = underTest.toResponse(badRequestException);
    }

    private void givenCreatedBadRequestException(final Exception exception) {
        badRequestException = new BadRequestException(EXCEPTION_MESSAGE, exception);
    }

    private void thenValidResponse(final ErrorCause errorCause, final MediaType mediaType) {
        Assert.assertNotNull(response);
        Object entity = response.getEntity();
        Assert.assertEquals(entity.getClass(), EhubError.class);
        EhubError ehubError = EhubError.class.cast(entity);
        Assert.assertEquals(ehubError.getCause(), errorCause);
        Assert.assertEquals(response.getMediaType(), mediaType);
    }


    private void givenMediaType(final MediaType mediaType) {
        given(headers.getMediaType()).willReturn(mediaType);
    }

    private void givenRequestUri(final String requestUri) {
        given(request.getRequestURI()).willReturn(requestUri);
    }
}
