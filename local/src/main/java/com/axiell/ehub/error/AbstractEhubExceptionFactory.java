package com.axiell.ehub.error;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProviderName;

import static com.axiell.ehub.ErrorCause.BAD_REQUEST;
import static com.axiell.ehub.ErrorCause.CONTENT_PROVIDER_ERROR;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME;

public abstract class AbstractEhubExceptionFactory implements IEhubExceptionFactory {

    @Override
    public InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(
            final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentValue.Type argValueType, final String language) {
        return createInternalServerErrorExceptionWithContentProviderNameAndStatus(null, contentProviderConsumer, argValueType, language);
    }

    @Override
    public InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(
            final String message, final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentValue.Type argValueType,
            final String language) {
        final ErrorCauseArgument contentProviderNameArg = makeContentProviderNameArg(contentProviderConsumer);
        final ErrorCauseArgument statusArg = makeStatusArg(contentProviderConsumer, argValueType, language);
        return message == null ? new InternalServerErrorException(CONTENT_PROVIDER_ERROR, contentProviderNameArg, statusArg) :
                new InternalServerErrorException(message, CONTENT_PROVIDER_ERROR, contentProviderNameArg, statusArg);
    }

    @Override
    public BadRequestException createBadRequestExceptionWithContentProviderNameAndStatus(final ContentProviderConsumer contentProviderConsumer,
                                                                                         final ErrorCauseArgumentValue.Type argValueType,
                                                                                         final String language) {
        final ErrorCauseArgument contentProviderNameArg = makeContentProviderNameArg(contentProviderConsumer);
        final ErrorCauseArgument statusArg = makeStatusArg(contentProviderConsumer, argValueType, language);
        return new BadRequestException(BAD_REQUEST, contentProviderNameArg, statusArg);
    }

    private ErrorCauseArgument makeContentProviderNameArg(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProviderName contentProviderName = contentProviderConsumer.getContentProvider().getName();
        return new ErrorCauseArgument(CONTENT_PROVIDER_NAME, contentProviderName);
    }

    protected abstract ErrorCauseArgument makeStatusArg(final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentValue.Type argValueType,
                                                        final String language);
}
