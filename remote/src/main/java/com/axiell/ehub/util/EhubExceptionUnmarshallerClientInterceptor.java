package com.axiell.ehub.util;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.logging.ToStringConverter;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

/**
 * Client Exception Interceptor.
 */
public class EhubExceptionUnmarshallerClientInterceptor implements ClientErrorInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EhubExceptionUnmarshallerClientInterceptor.class);

    /**
     * @see org.jboss.resteasy.client.core.ClientErrorInterceptor#handle(org.jboss.resteasy.client.ClientResponse)
     */
    @Override
    public void handle(final ClientResponse<?> clientResponse) throws EhubRuntimeException {
        MediaType mediaType = clientResponse.getMediaType();
        if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE) || mediaType.equals(MediaType.APPLICATION_XML_TYPE)) {
            EhubError ehubError = clientResponse.getEntity(EhubError.class);
            if (ehubError == null) {
                ehubError = ErrorCause.INTERNAL_SERVER_ERROR.toEhubError();
            }
            LOGGER.error(ehubError.getMessage());
            throw new EhubRuntimeException(clientResponse.getStatus(), ehubError);
        } else {
            LOGGER.error(" Response:" + ToStringConverter.lineFeed() + ToStringConverter.clientResponseToString(clientResponse));
        }
    }
}
