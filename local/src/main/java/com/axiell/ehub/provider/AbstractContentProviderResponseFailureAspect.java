package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import org.aspectj.lang.JoinPoint;

public class AbstractContentProviderResponseFailureAspect {
    protected static final int UNKNOWN_STATUS_CODE = -1;
    protected static final String UNKNOWN_CONTENT_PROVIDER = "unknown";
    protected static final String DEFAULT_MESSAGE = "An unepected exception occurred while trying to connect to the Content Provider";


    protected ErrorCauseArgument makeContentProviderNameErrorCauseArgument(final ContentProvider contentProvider) {
        final Object contentProviderName = contentProvider == null ? UNKNOWN_CONTENT_PROVIDER : contentProvider.getName();
        return new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, contentProviderName);
    }

    protected ContentProvider getContentProvider(JoinPoint joinPoint) {
        ContentProviderConsumer contentProviderConsumer = getContentProviderConsumer(joinPoint);
        return contentProviderConsumer == null ? null : contentProviderConsumer.getContentProvider();
    }

    protected ContentProviderConsumer getContentProviderConsumer(JoinPoint joinPoint) {
        final CommandData commandData = getCommandData(joinPoint);
        return commandData == null ? null : commandData.getContentProviderConsumer();
    }

    protected CommandData getCommandData(final JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof CommandData)
                return (CommandData) arg;
        }
        return null;
    }

}
