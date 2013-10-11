package com.axiell.ehub.logging;

import com.axiell.ehub.util.EhubUrlCodec;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.HttpRequest;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Converts objects to human readable strings. Useful with debugging.
 */

public final class ToString {
    private static final Logger LOGGER = getLogger(ToString.class);
    private static final DateTimeFormatter DATE_FORMAT = forPattern("yyyy-MM-dd HH:mm:ss");
    private static final RecursiveToStringStyle RECURSIVE_TO_STRING_STYLE = new RecursiveToStringStyle();
    private static final String HEADERS_TITLE = "Headers: ";
    private static final String ENTITY_TITLE = "Entity: ";
    private static final String PARAMETERS_TITLE = "Parameters: ";
    private static final String PAYLOAD_TITLE = "Payload: ";
    private static final String RESPONSE_CODE_TITLE = "Response-Code: ";
    private static final String METADATA_TITLE = "Metadata: ";
    private static final String BLANK_SPACE = " ";

    /**
     * Private constructor that prevents direct instantiation.
     */
    private ToString() {
    }

    public static String clientRequestToString(final ClientRequest clientRequestToProcess) {
        final StringBuilder clientRequestStringBuilder = newStringBuilder();
        appendHttpMethod(clientRequestToProcess.getHttpMethod(), clientRequestStringBuilder);
        appendUri(clientRequestToProcess, clientRequestStringBuilder);
        appendFormOrPathParameters(clientRequestToProcess.getFormParameters(), clientRequestStringBuilder);
        appendFormOrPathParameters(clientRequestToProcess.getPathParameters(), clientRequestStringBuilder);
        appendHeaders(clientRequestToProcess.getHeaders(), clientRequestStringBuilder);
        appendBody(clientRequestToProcess.getBody(), clientRequestStringBuilder);
        return clientRequestStringBuilder.toString();
    }

    public static String clientResponseToString(final ClientResponse<?> clientResponseToProcess) {
        StringBuilder clientResponseStringBuilder = newStringBuilder();
        appendResponseStatus(clientResponseToProcess.getStatus(), clientResponseStringBuilder);
        appendResponseReason(clientResponseToProcess, clientResponseStringBuilder);
        appendHeaders(clientResponseToProcess.getHeaders(), clientResponseStringBuilder);
        readAndAppendResponseBody(clientResponseToProcess, clientResponseStringBuilder);
        return clientResponseStringBuilder.toString();
    }

    public static String httpRequestToString(final HttpRequest httpRequestToProcess) {
        StringBuilder httpRequestStringBuilder = newStringBuilder();
        appendHttpMethod(httpRequestToProcess.getHttpMethod(), httpRequestStringBuilder);
        appendStringURIFromHttpRequest(httpRequestToProcess, httpRequestStringBuilder);
        appendFormOrPathParameters(httpRequestToProcess.getDecodedFormParameters(), httpRequestStringBuilder);
        appendHttpHeadersFromHttpRequest(httpRequestToProcess, httpRequestStringBuilder);
        return httpRequestStringBuilder.toString();
    }

    public static String serverResponseToString(final ServerResponse serverResponse) {
        final StringBuilder serverResponseStringBuilder = newStringBuilder();
        appendResponseStatus(serverResponse.getStatus(), serverResponseStringBuilder);
        appendMetaDataFromServerResponse(serverResponse, serverResponseStringBuilder);
        appendBody(serverResponse.getEntity(), serverResponseStringBuilder);
        return serverResponseStringBuilder.toString();
    }

    public static String soapMessageToString(final SOAPMessage soapMessageToProcess) {
        StringBuilder soapMessageStringBuilder = newStringBuilder();
        StringBufferOutputStream soapMessageStream = new StringBufferOutputStream();
        appendSoapMessage(soapMessageToProcess, soapMessageStringBuilder, soapMessageStream);
        return soapMessageStringBuilder.toString();
    }

    public static String dateToString(final Date dateToProcess) {
        return DATE_FORMAT.print(dateToProcess.getTime());
    }

    public static String mapToString(final Map<?, ?> mapToProcess) {
        return RECURSIVE_TO_STRING_STYLE.toString(mapToProcess);
    }

    public static String collectionToString(final Collection<?> collectionToProcess) {
        return RECURSIVE_TO_STRING_STYLE.toString(collectionToProcess);
    }

    public static String objectToString(final Object objectToProcess) {
        return reflectionToString(objectToProcess, RECURSIVE_TO_STRING_STYLE);
    }

    public static String lineFeed() {
        return LINE_SEPARATOR;
    }

    private static void appendMetaDataFromServerResponse(final ServerResponse serverResponse, final StringBuilder serverResponseStringBuilder) {
        if (thereIsMetadataIn(serverResponse)) {
            serverResponseStringBuilder.append(lineFeed());
            serverResponseStringBuilder.append(METADATA_TITLE);
            serverResponseStringBuilder.append(mapToString(serverResponse.getMetadata()));
        }
    }

    private static boolean thereIsMetadataIn(final ServerResponse serverResponse) {
        return !serverResponse.getMetadata().isEmpty();
    }

    private static StringBuilder newStringBuilder() {
        return new StringBuilder();
    }


    private static void appendHttpHeadersFromHttpRequest(final HttpRequest httpRequestToProcess, final StringBuilder httpRequestStringBuilder) {
        if (thereAreHttpHeadersIn(httpRequestToProcess)) {
            appendHeaders(httpRequestToProcess.getHttpHeaders().getRequestHeaders(), httpRequestStringBuilder);
        }
    }

    private static boolean thereAreHttpHeadersIn(final HttpRequest httpRequestToProcess) {
        return httpRequestToProcess.getHttpHeaders() != null && httpRequestToProcess.getHttpHeaders().getRequestHeaders() != null &&
                !httpRequestToProcess.getHttpHeaders().getRequestHeaders().isEmpty();
    }

    private static void appendStringURIFromHttpRequest(final HttpRequest httpRequestToProcess, final StringBuilder httpRequestStringBuilder) {
        httpRequestStringBuilder.append(httpRequestToProcess.getUri().getRequestUri());
    }

    private static void appendHttpMethod(final String httpMethodString, final StringBuilder sb) {
        sb.append(httpMethodString).append(BLANK_SPACE);
    }

    private static void appendBody(final Object body, final StringBuilder sb) {
        if (body != null) {
            sb.append(lineFeed());
            sb.append(ENTITY_TITLE);
            sb.append(objectToString(body));
        }
    }

    private static void appendHeaders(final MultivaluedMap<String, String> headersToProcess, final StringBuilder sb) {
        if (thereAreValuesToAppendIn(headersToProcess)) {
            sb.append(lineFeed());
            sb.append(HEADERS_TITLE);
            sb.append(mapToString(headersToProcess));
        }
    }


    private static void appendFormOrPathParameters(final MultivaluedMap<String, String> formParameters, final StringBuilder sb) {
        if (thereAreValuesToAppendIn(formParameters)) {
            sb.append(lineFeed());
            sb.append(PARAMETERS_TITLE);
            sb.append(mapToString(formParameters));
        }
    }

    private static boolean thereAreValuesToAppendIn(final MultivaluedMap<String, String> mapToProcess) {
        return mapToProcess != null && !mapToProcess.isEmpty();
    }

    private static void appendUri(final ClientRequest clientRequest, final StringBuilder sb) {
        try {
            sb.append(clientRequest.getUri());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private static void readAndAppendResponseBody(final ClientResponse<?> clientResponseToProcess, final StringBuilder clientResponseStringBuilder) {
        String body = null;
        try {
            body = readClientResponseStream(clientResponseToProcess);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        doAppendResponseBody(clientResponseStringBuilder, body);

    }

    private static void doAppendResponseBody(final StringBuilder clientResponseStringBuilder, final String body) {
        if (bodyShouldBeAppended(body)) {
            clientResponseStringBuilder.append(lineFeed());
            clientResponseStringBuilder.append(PAYLOAD_TITLE);
            clientResponseStringBuilder.append(body);
        }
    }

    private static boolean bodyShouldBeAppended(final String body) {
        return body != null && !body.isEmpty();
    }

    private static String readClientResponseStream(final ClientResponse<?> clientResponseToProcess) throws IOException {
        InputStream io = ((BaseClientResponse<?>) clientResponseToProcess).getStreamFactory().getInputStream();
        final String body = IOUtils.toString(io, EhubUrlCodec.UTF8);
        clientResponseToProcess.resetStream();
        return body;
    }

    private static void appendResponseReason(final ClientResponse<?> clientResponseToProcess, final StringBuilder clientResponseStringBuilder) {
        if (thereIsAResponseStatusReasonIn(clientResponseToProcess)) {
            clientResponseStringBuilder.append(BLANK_SPACE);
            clientResponseStringBuilder.append(clientResponseToProcess.getResponseStatus().getReasonPhrase());
        }
    }

    private static boolean thereIsAResponseStatusReasonIn(final ClientResponse<?> clientResponseToProcess) {
        return clientResponseToProcess.getResponseStatus() != null && StringUtils.isNotBlank(clientResponseToProcess.getResponseStatus().getReasonPhrase());
    }

    private static void appendResponseStatus(final int responseCode, final StringBuilder sb) {
        sb.append(RESPONSE_CODE_TITLE).append(responseCode);
    }


    private static void appendSoapMessage(final SOAPMessage soapMessageToProcess, final StringBuilder soapMessageStringBuilder, final StringBufferOutputStream soapMessageStream) {
        readSoapMessage(soapMessageToProcess, soapMessageStream);
        soapMessageStringBuilder.append(soapMessageStream.toString());
    }

    private static void readSoapMessage(final SOAPMessage soapMessageToProcess, final StringBufferOutputStream soapMessageStream) {
        try {
            soapMessageToProcess.writeTo(soapMessageStream);
        } catch (SOAPException | IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }


}