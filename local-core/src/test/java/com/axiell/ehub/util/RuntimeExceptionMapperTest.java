package com.axiell.ehub.util;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.controller.provider.mapper.RuntimeExceptionMapper;
import com.axiell.ehub.security.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RuntimeExceptionMapperTest {
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";

    private RuntimeExceptionMapper underTest;

    @Mock
    private HttpHeaders headers;

    @Mock
    private HttpServletRequest request;

    private RuntimeException runtimeException;
    private Response response;

    @BeforeEach
    public void setUp() {
        underTest = new RuntimeExceptionMapper();
        underTest.setHeaders(headers);
        underTest.setRequest(request);
    }

    @Test
    public void nullPointerException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedRuntimeException(new NullPointerException());
        whenResponseGenerated();
        thenValidResponse(ErrorCause.INTERNAL_SERVER_ERROR, MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void nullPointerExceptionXml() {
        givenMediaType(MediaType.APPLICATION_XML_TYPE);
        givenCreatedRuntimeException(new NullPointerException());
        whenResponseGenerated();
        thenValidResponse(ErrorCause.INTERNAL_SERVER_ERROR, MediaType.APPLICATION_XML_TYPE);
    }

    @Test
    public void unauthorizedException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedRuntimeException(new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.MISSING_AUTHORIZATION_HEADER, MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void internalServerErrorException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedRuntimeException(new InternalServerErrorException(ErrorCause.CONTENT_PROVIDER_ERROR));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.CONTENT_PROVIDER_ERROR, MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void forbiddenException() {
        givenMediaType(MediaType.APPLICATION_JSON_TYPE);
        givenCreatedRuntimeException(new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED));
        whenResponseGenerated();
        thenValidResponse(ErrorCause.LMS_CHECKOUT_DENIED, MediaType.APPLICATION_JSON_TYPE);
    }

    private void whenResponseGenerated() {
        response = underTest.toResponse(runtimeException);
    }

    private void givenCreatedRuntimeException(final Exception exception) {
        runtimeException = new RuntimeException(EXCEPTION_MESSAGE, exception);
    }

    private void thenValidResponse(final ErrorCause errorCause, final MediaType mediaType) {
        Assertions.assertNotNull(response);
        Object entity = response.getEntity();
        Assertions.assertEquals(entity.getClass(), EhubError.class);
        EhubError ehubError = EhubError.class.cast(entity);
        Assertions.assertEquals(ehubError.getCause(), errorCause);
        Assertions.assertEquals(response.getMediaType(), mediaType);
    }


    private void givenMediaType(final MediaType mediaType) {
        given(headers.getMediaType()).willReturn(mediaType);
    }

    private void givenRequestUri(final String requestUri) {
        given(request.getRequestURI()).willReturn(requestUri);
    }
}
