package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

public abstract class AbstractContentProviderExceptionFactory<E> implements IContentProviderExceptionFactory<E> {
    public static final String DEFAULT_MESSAGE = "An unepected exception occurred while trying to connect to the Content Provider";
    protected static final int UNKNOWN_STATUS_CODE = -1;
    protected static final String UNKNOWN_CONTENT_PROVIDER = "unknown";
    private final Class<E> clazz;

    public AbstractContentProviderExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                                   final IEhubExceptionFactory ehubExceptionFactory, final Class<E> clazz) {
        this.clazz = clazz;
        this.contentProviderConsumer = contentProviderConsumer;
        this.language = language;
        this.ehubExceptionFactory = ehubExceptionFactory;
    }

    private IEhubExceptionFactory ehubExceptionFactory;
    private ContentProviderConsumer contentProviderConsumer;
    private String language;

    @Override
    public InternalServerErrorException create(final Response response) {
        String status = null;
        String message;
        try {
            E entity = readEntity(response, clazz);
            status = getStatus(entity);
            message = getMessage(entity);
        } catch (ProcessingException ex) {
            message = readEntity(response, String.class);
        }
        if (StringUtils.isBlank(message)) {
            message = DEFAULT_MESSAGE;
        }
        ErrorCauseArgumentValue.Type type = getErrorCauseArgumentValueType(status, message);
        if (type != null) {
            return ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(message, contentProviderConsumer, type, language);
        }
        if (StringUtils.isBlank(status)) {
            status = String.valueOf(getStatusCode(response));
        }
        final ErrorCauseArgument nameArg = makeContentProviderNameErrorCauseArgument(contentProviderConsumer.getContentProvider());
        final ErrorCauseArgument statusArg = makeStatusCodeErrorCauseArgument(status);
        return new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, nameArg, statusArg);
    }

    protected int getStatusCode(final Response response) {
        final Response.StatusType status = response.getStatusInfo();
        return status == null ? UNKNOWN_STATUS_CODE : status.getStatusCode();
    }

    protected ErrorCauseArgument makeStatusCodeErrorCauseArgument(final String status) {
        return new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS, status);
    }

    protected ErrorCauseArgument makeContentProviderNameErrorCauseArgument(final ContentProvider contentProvider) {
        final Object contentProviderName = contentProvider == null ? UNKNOWN_CONTENT_PROVIDER : contentProvider.getName();
        return new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, contentProviderName);
    }

    @SuppressWarnings("unchecked")
    private <T> T readEntity(final Response response, final Class<T> clazz) {
        if (response instanceof ClientResponse) {
            return ((ClientResponse<T>) response).getEntity(clazz);
        } else {
            return response.readEntity(clazz);
        }
    }

    protected abstract String getStatus(final E entity);

    protected abstract String getMessage(final E entity);

    protected abstract ErrorCauseArgumentValue.Type getErrorCauseArgumentValueType(final String status, final String message);
}
