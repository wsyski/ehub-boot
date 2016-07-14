package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

public abstract class AbstractContentProviderExceptionFactory<E> implements IContentProviderExceptionFactory<E> {
    static final String DEFAULT_MESSAGE = "An unepected exception occurred while trying to connect to the Content Provider";
    public final String UNKNOWN_CONTENT_PROVIDER = "unknown";
    public static final String UNKNOWN_STATUS_CODE = "unknown";
    private final Class<E> clazz;
    private IEhubExceptionFactory ehubExceptionFactory;
    private ContentProviderConsumer contentProviderConsumer;
    private String language;

    public AbstractContentProviderExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                                   final IEhubExceptionFactory ehubExceptionFactory, final Class<E> clazz) {
        this.clazz = clazz;
        this.contentProviderConsumer = contentProviderConsumer;
        this.language = language;
        this.ehubExceptionFactory = ehubExceptionFactory;
    }

    @Override
    public InternalServerErrorException create(final Response response) {
        String code = null;
        String message;
        try {
            E entity = readEntity(response, clazz);
            code = getCode(entity);
            message = getMessage(entity);
        } catch (ProcessingException ex) {
            message = readEntity(response, String.class);
        }
        if (StringUtils.isBlank(message)) {
            message = DEFAULT_MESSAGE;
        }
        ErrorCauseArgumentType type = getErrorCauseArgumentValueType(code, message);
        if (type != null) {
            return ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(message, contentProviderConsumer, type, language);
        }
        if (StringUtils.isBlank(code)) {
            Integer statusCode = getStatusCode(response);
            code = statusCode == null ? UNKNOWN_STATUS_CODE : String.valueOf(statusCode);
        }
        return makeInternalServerErrorException(code, message);
    }

    @Override
    public InternalServerErrorException create(E entity) {
        String code = getCode(entity);
        String message = getMessage(entity);

        if (StringUtils.isBlank(message)) {
            message = DEFAULT_MESSAGE;
        }
        ErrorCauseArgumentType type = getErrorCauseArgumentValueType(code, message);
        if (type != null) {
            return ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(message, contentProviderConsumer, type, language);
        }
        if (StringUtils.isBlank(code)) {
            code = UNKNOWN_STATUS_CODE;
        }
        return makeInternalServerErrorException(code, message);
    }

    private InternalServerErrorException makeInternalServerErrorException(String code, String message) {
        final ErrorCauseArgument nameArg = makeContentProviderNameErrorCauseArgument(contentProviderConsumer.getContentProvider());
        final ErrorCauseArgument statusArg = makeStatusCodeErrorCauseArgument(code);
        return new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, nameArg, statusArg);
    }

    protected Integer getStatusCode(final Response response) {
        final Response.StatusType status = response.getStatusInfo();
        return status == null ? null : status.getStatusCode();
    }

    protected ErrorCauseArgument makeStatusCodeErrorCauseArgument(final String code) {
        return new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS, code);
    }

    protected ErrorCauseArgument makeContentProviderNameErrorCauseArgument(final ContentProvider contentProvider) {
        final Object contentProviderName = contentProvider == null ? UNKNOWN_CONTENT_PROVIDER : contentProvider.getName();
        return new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, contentProviderName);
    }

    @SuppressWarnings("unchecked")
    private <T> T readEntity(final Response response, final Class<T> clazz) {
        return response.readEntity(clazz);
    }

    protected abstract String getCode(final E entity);

    protected abstract String getMessage(final E entity);

    protected abstract ErrorCauseArgumentType getErrorCauseArgumentValueType(final String code, final String message);
}
