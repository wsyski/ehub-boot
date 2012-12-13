package com.axiell.ehub.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.HttpRequest;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Converts objects to human readable strings. Useful with debugging.
 */

public class ToString {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToString.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final RecursiveToStringStyle RECURSIVE_TO_STRING_STYLE = new RecursiveToStringStyle();
    private static final String LF = SystemUtils.LINE_SEPARATOR;

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
            sb.append(fromMap(clientRequest.getFormParameters()));
        }
        if (!clientRequest.getPathParameters().isEmpty()) {
            sb.append(LF);
            sb.append("Parameters: ");
            sb.append(fromMap(clientRequest.getPathParameters()));
        }
        if (!clientRequest.getHeaders().isEmpty()) {
            sb.append(LF);
            sb.append("Headers: ");
            sb.append(fromMap(clientRequest.getHeaders()));
        }
        Object body = clientRequest.getBody();
        if (body != null) {
            sb.append(LF);
            sb.append("Body: ");
            sb.append(fromObject(body));
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
            sb.append(fromMap(clientResponse.getHeaders()));
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
            sb.append(fromMap(httpRequest.getDecodedFormParameters()));
        }
        if (httpRequest.getHttpHeaders() != null && httpRequest.getHttpHeaders().getRequestHeaders() != null &&
                !httpRequest.getHttpHeaders().getRequestHeaders().isEmpty()) {
            sb.append(LF);
            sb.append("Headers: ");
            sb.append(fromMap(httpRequest.getHttpHeaders().getRequestHeaders()));
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
            sb.append(fromMap(serverResponse.getMetadata()));
        }
        Object entity = serverResponse.getEntity();
        if (entity != null) {
            sb.append(LF);
            sb.append("Entity: ");
            sb.append(fromObject(entity));
        }
        return sb.toString();
    }

    public static String fromDate(final Date date) {
        return DATE_FORMAT.print(date.getTime());
    }

    public static String fromMap(final Map<?, ?> map) {
        return RECURSIVE_TO_STRING_STYLE.toString(map);
    }

    public static String fromCollection(final Collection<?> collection) {
        return RECURSIVE_TO_STRING_STYLE.toString(collection);
    }

    public static String fromObject(final Object object) {
        return ToStringBuilder.reflectionToString(object, RECURSIVE_TO_STRING_STYLE);
    }

    public static String getLf() {
        return LF;
    }

    private static class RecursiveToStringStyle extends ToStringStyle {
        private static final String UNKNOWN_FIELD = "?";


        public RecursiveToStringStyle() {
            setArrayContentDetail(true);
            setUseShortClassName(true);
            setUseClassName(false);
            setUseIdentityHashCode(false);
            setFieldSeparator(", " + SystemUtils.LINE_SEPARATOR + "  ");
        }

        @Override
        public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if (value instanceof Date) {
                try {
                    buffer.append(fromDate((Date) value));
                } catch (RuntimeException ex) {
                    buffer.append(UNKNOWN_FIELD);
                }
            } else if (value.getClass().getName().startsWith("java.")) {
                try {
                    buffer.append(value);
                } catch (RuntimeException ex) {
                    buffer.append(UNKNOWN_FIELD);
                }
            } else {
                try {
                    buffer.append(ReflectionToStringBuilder.toString(value, this));
                } catch (RuntimeException ex) {
                    buffer.append(UNKNOWN_FIELD);
                }
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
            Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
            if (!iterator.hasNext()) {
                buffer.append("{}");
            } else {
                buffer.append('{');
                for (; ; ) {
                    Map.Entry e = iterator.next();
                    Object key = e.getKey();
                    Object value = e.getValue();
                    if (key == this) {
                        buffer.append("(this Map)");
                    } else {
                        appendInternal(buffer, fieldName, key, true);
                    }
                    buffer.append('=');
                    if (value == this) {
                        buffer.append("(this Map)");
                    } else {
                        appendInternal(buffer, fieldName, value, true);
                    }
                    if (!iterator.hasNext()) {
                        buffer.append('}');
                        break;
                    }
                    buffer.append(',').append(' ');
                }
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
            Iterator iterator = coll.iterator();
            if (!iterator.hasNext()) {
                buffer.append("[]");
            } else {
                buffer.append('[');
                for (; ; ) {
                    Object value = iterator.next();
                    if (value == this) {
                        buffer.append("(this Collection)");
                    } else {
                        appendInternal(buffer, fieldName, value, true);
                    }
                    if (!iterator.hasNext()) {
                        buffer.append(']');
                        break;
                    }
                    buffer.append(',').append(' ');
                }
            }
        }


        public String toString(final Map<?, ?> map) {
            StringBuffer buffer = new StringBuffer();
            appendDetail(buffer, null, map);
            return buffer.toString();
        }

        public String toString(Collection<?> collection) {
            StringBuffer buffer = new StringBuffer();
            appendDetail(buffer, null, collection);
            return buffer.toString();
        }
    }
}