package com.axiell.ehub.util;

import com.google.common.base.Joiner;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Converts objects to human readable strings. Useful with debugging.
 */

public class ToString {
    private static final Logger LOGGER = Logger.getLogger(ToString.class);
    private static final RecursiveToStringStyle RECURSIVE_TO_STRING_STYLE= new RecursiveToStringStyle();
    private static final String LF = System.getProperty("line.separator");

    public static String fromClientRequest(final ClientRequest clientRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(clientRequest.getHttpMethod());
        sb.append(" ");
        try {
            sb.append(clientRequest.getUri());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        if (!clientRequest.getFormParameters().isEmpty()) {
            sb.append(LF);
            sb.append("Parameters: ");
            getMapJoiner().appendTo(sb, clientRequest.getFormParameters());
        }
        if (!clientRequest.getPathParameters().isEmpty()) {
            sb.append(LF);
            sb.append("Parameters: ");
            getMapJoiner().appendTo(sb, clientRequest.getPathParameters());
        }
        if (!clientRequest.getHeaders().isEmpty()) {
            sb.append(LF);
            sb.append("Headers: ");
            getMapJoiner().appendTo(sb, clientRequest.getHeaders());
        }
        Object body = clientRequest.getBody();
        if (body != null) {
            sb.append(LF);
            sb.append("Body: ");
            sb.append(ToStringBuilder.reflectionToString(body, RECURSIVE_TO_STRING_STYLE));
        }
        return sb.toString();
    }

    public static String fromClientResponse(final ClientResponse clientResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append("Response-Code: ");
        sb.append(clientResponse.getResponseStatus().getStatusCode());
        if (StringUtils.isNotBlank(clientResponse.getResponseStatus().getReasonPhrase())) {
            sb.append(" ");
            sb.append(clientResponse.getResponseStatus().getReasonPhrase());
        }
        if (!clientResponse.getHeaders().isEmpty()) {
            sb.append(LF);
            sb.append("Headers: ");
            getMapJoiner().appendTo(sb, clientResponse.getHeaders());
        }
        String body = null;
        try {
            InputStream io = ((BaseClientResponse) clientResponse).getStreamFactory().getInputStream();
            body = IOUtils.toString(io, EhubUrlCodec.UTF8);
            clientResponse.resetStream();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        if (body != null && !body.isEmpty()) {
            sb.append(LF);
            sb.append("Payload: ");
            sb.append(body);
        }
        return sb.toString();
    }

    public static String fromHttpRequest(final HttpRequest httpRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(httpRequest.getHttpMethod());
        sb.append(" ");
        sb.append(httpRequest.getUri().getRequestUri());
        if (!httpRequest.getDecodedFormParameters().isEmpty()) {
            sb.append(LF);
            sb.append("Parameters: ");
            getMapJoiner().appendTo(sb, httpRequest.getDecodedFormParameters());
        }
        if (httpRequest.getHttpHeaders() != null && httpRequest.getHttpHeaders().getRequestHeaders() != null &&
                !httpRequest.getHttpHeaders().getRequestHeaders().isEmpty()) {
            sb.append(LF);
            sb.append("Headers: ");
            getMapJoiner().appendTo(sb, httpRequest.getHttpHeaders().getRequestHeaders());
        }
        return sb.toString();
    }

    public static String fromServerResponse(final ServerResponse serverResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append("Response-Code: ");
        sb.append(serverResponse.getStatus());
        if (!serverResponse.getMetadata().isEmpty()) {
            sb.append(LF);
            sb.append("Metadata: ");
            getMapJoiner().appendTo(sb, serverResponse.getMetadata());
        }
        Object entity = serverResponse.getEntity();
        if (entity != null) {
            sb.append(LF);
            sb.append("Entity: ");
            sb.append(ToStringBuilder.reflectionToString(entity, RECURSIVE_TO_STRING_STYLE));
        }
        return sb.toString();
    }

    private static Joiner.MapJoiner getMapJoiner() {
        Joiner.MapJoiner joiner = Joiner.on(",").withKeyValueSeparator("->");
        return joiner;
    }

    public static String getLf() {
        return LF;
    }


    private static class RecursiveToStringStyle extends ToStringStyle {
        private static final String UNKNOWN_FIELD = "?";
        private static final int INFINITE_DEPTH = -1;

        /**
         * Setting {@link #maxDepth} to 0 will have the same effect as using original {@link #ToStringStyle}: it will
         * print all 1st level values without traversing into them. Setting to 1 will traverse up to 2nd level and so
         * on.
         */
        private int maxDepth;

        private int depth;

        public RecursiveToStringStyle() {
            this(INFINITE_DEPTH);
        }

        public RecursiveToStringStyle(int maxDepth) {
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
            this.maxDepth = maxDepth;
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if (value.getClass().getName().startsWith("java.lang.") || (maxDepth != INFINITE_DEPTH && depth >= maxDepth)) {
                try {
                    buffer.append(value);
                } catch (LazyInitializationException ex) {
                    buffer.append(UNKNOWN_FIELD);
                }
            } else {
                depth++;
                try {
                    buffer.append(ReflectionToStringBuilder.toString(value, this));
                } catch (LazyInitializationException ex) {
                    buffer.append(UNKNOWN_FIELD);
                }
                depth--;
            }
        }

        // another helpful method
        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
            depth++;
            buffer.append(ReflectionToStringBuilder.toString(coll.toArray(), this, true, true));
            depth--;
        }
    }
}