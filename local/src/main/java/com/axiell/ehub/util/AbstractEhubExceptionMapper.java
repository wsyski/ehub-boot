/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class AbstractEhubExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractEhubExceptionMapper.class);

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
            String requestUri = request.getRequestURI();
            if (requestUri != null) {
                if (requestUri.startsWith("/v1")) {
                    return MediaType.APPLICATION_XML_TYPE;
                } else {
                    return MediaType.APPLICATION_JSON_TYPE;
                }
            } else {
                return MediaType.APPLICATION_JSON_TYPE;
            }
        }
    }

    private boolean isSupportedMediaType(final MediaType mediaType) {
        return MediaType.APPLICATION_JSON_TYPE.equals(mediaType) || MediaType.APPLICATION_XML_TYPE.equals(mediaType);
    }

    void setHeaders(final HttpHeaders headers) {
        this.headers = headers;
    }

    void setRequest(final HttpServletRequest request) {
        this.request = request;
    }
}
