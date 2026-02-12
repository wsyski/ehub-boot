package com.axiell.ehub.core.controller.provider.mapper;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

@Slf4j
public abstract class AbstractEhubExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {

    @Context
    private HttpHeaders headers;

    @Context
    private HttpServletRequest request;

    @Override
    public abstract Response toResponse(E exception);


    protected MediaType getMediaType() {
        MediaType mediaType = headers.getMediaType();
        if (isSupportedMediaType(mediaType)) {
            return mediaType;
        } else {
            return MediaType.APPLICATION_JSON_TYPE;
        }
    }

    private boolean isSupportedMediaType(final MediaType mediaType) {
        return MediaType.APPLICATION_JSON_TYPE.equals(mediaType) || MediaType.APPLICATION_XML_TYPE.equals(mediaType);
    }

    public void setHeaders(final HttpHeaders headers) {
        this.headers = headers;
    }

    public void setRequest(final HttpServletRequest request) {
        this.request = request;
    }
}
