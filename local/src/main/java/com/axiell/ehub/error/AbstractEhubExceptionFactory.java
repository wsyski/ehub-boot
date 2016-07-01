package com.axiell.ehub.error;

import com.axiell.ehub.*;
import com.axiell.ehub.consumer.ContentProviderConsumer;


import static com.axiell.ehub.ErrorCause.BAD_REQUEST;
import static com.axiell.ehub.ErrorCause.CONTENT_PROVIDER_ERROR;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME;

public abstract class AbstractEhubExceptionFactory implements IEhubExceptionFactory {

    @Override
    public InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(
            final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentType argValueType, final String language) {
        return createInternalServerErrorExceptionWithContentProviderNameAndStatus(null, contentProviderConsumer, argValueType, language);
    }

    @Override
    public InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(
            final String message, final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentType argValueType,
            final String language) {
        final ErrorCauseArgument contentProviderNameArg = makeContentProviderNameArg(contentProviderConsumer);
        final ErrorCauseArgument statusArg = makeStatusArg(contentProviderConsumer, argValueType, language);
        InternalServerErrorException internalServerErrorException = message == null ? new InternalServerErrorException(CONTENT_PROVIDER_ERROR, contentProviderNameArg, statusArg) :
                new InternalServerErrorException(message, CONTENT_PROVIDER_ERROR, contentProviderNameArg, statusArg);
        return internalServerErrorException;
    }

    @Override
    public BadRequestException createBadRequestExceptionWithContentProviderNameAndStatus(final ContentProviderConsumer contentProviderConsumer,
                                                                                         final ErrorCauseArgumentType argValueType,
                                                                                         final String language) {
        final ErrorCauseArgument contentProviderNameArg = makeContentProviderNameArg(contentProviderConsumer);
        final ErrorCauseArgument statusArg = makeStatusArg(contentProviderConsumer, argValueType, language);
        BadRequestException badRequestException = new BadRequestException(BAD_REQUEST, contentProviderNameArg, statusArg);
        return  badRequestException;
    }

    private ErrorCauseArgument makeContentProviderNameArg(final ContentProviderConsumer contentProviderConsumer) {
        final String contentProviderName = contentProviderConsumer.getContentProvider().getName();
        return new ErrorCauseArgument(CONTENT_PROVIDER_NAME, contentProviderName);
    }

    protected abstract ErrorCauseArgument makeStatusArg(final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentType argValueType,
                                                        final String language);
}
