package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractContentProviderResponseFailureAspect {

    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    protected IContentProviderExceptionFactory getContentProviderErrorExceptionFactory(final JoinPoint joinPoint) {
        final CommandData commandData = getCommandData(joinPoint);
        final ContentProviderConsumer contentProviderConsumer = getContentProviderConsumer(commandData);
        final String language = getLanguage(commandData);
        return ContentProviderExceptionFactoryResolver.create(contentProviderConsumer, language, ehubExceptionFactory);
    }

    private ContentProviderConsumer getContentProviderConsumer(final CommandData commandData) {
        return commandData == null ? null : commandData.getContentProviderConsumer();
    }

    private String getLanguage(final CommandData commandData) {
        return commandData == null ? "en" : commandData.getLanguage();
    }

    private CommandData getCommandData(final JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof CommandData)
                return (CommandData) arg;
        }
        return null;
    }
}
