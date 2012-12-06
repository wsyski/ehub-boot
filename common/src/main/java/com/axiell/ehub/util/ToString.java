package com.axiell.ehub.util;

import com.google.common.base.Joiner;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * Dumps ehub classes.
 */

public class ToString {
    private static final Logger LOGGER = Logger.getLogger(ToString.class);
    private static final String LF = System.getProperty("line.separator");

    public static String fromClientRequest(final ClientRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getHttpMethod());
        sb.append(" ");
        try {
            sb.append(request.getUri());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        if (!request.getFormParameters().isEmpty()) {
            sb.append(LF);
            sb.append("Parameters: ");
            getMapJoiner().appendTo(sb, request.getFormParameters());
        }
        if (!request.getPathParameters().isEmpty()) {
            sb.append(LF);
            sb.append("Parameters: ");
            getMapJoiner().appendTo(sb, request.getPathParameters());
        }
        if (!request.getHeaders().isEmpty()) {
            sb.append(LF);
            sb.append("Headers: ");
            getMapJoiner().appendTo(sb, request.getHeaders());
        }
        Object body = request.getBody();
        if (body != null) {
            sb.append(LF);
            sb.append("Payload: ");
            sb.append(ToStringBuilder.reflectionToString(body, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return sb.toString();
    }

    public static String fromClientResponse(final ClientResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Response-Code: ");
        sb.append(response.getResponseStatus().getStatusCode());
        if (StringUtils.isNotBlank(response.getResponseStatus().getReasonPhrase())) {
            sb.append(" ");
            sb.append(response.getResponseStatus().getReasonPhrase());
        }
        if (!response.getHeaders().isEmpty()) {
            sb.append(LF);
            sb.append("Headers: ");
            getMapJoiner().appendTo(sb, response.getHeaders());
        }
        String body = null;
        try {
            InputStream io = ((BaseClientResponse) response).getStreamFactory().getInputStream();
            body = IOUtils.toString(io, EhubUrlCodec.UTF8);
            response.resetStream();
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

    private static Joiner.MapJoiner getMapJoiner() {
        Joiner.MapJoiner joiner = Joiner.on(",").withKeyValueSeparator("->");
        return joiner;
    }

    public static String getLf() {
        return LF;
    }
}
