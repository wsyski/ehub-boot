package com.axiell.ehub.provider.askews;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class AskewsExceptionFactory extends AbstractContentProviderExceptionFactory<String> implements IContentProviderExceptionFactory<String> {
    static final String MESSAGE_NO_LICENCES_AVAILABLE = "No Licences available";

    public AskewsExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                  final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory,String.class);
    }

    @Override
    protected String getCode(final String error) {
        return null;
    }

    @Override
    protected String getMessage(final String error) {
        return error;
    }

    @Override
    protected ErrorCauseArgumentType getErrorCauseArgumentValueType(final String code, final String message) {
        ErrorCauseArgumentType type = null;
        if (message != null) {
            if (message.contains(MESSAGE_NO_LICENCES_AVAILABLE)) {
                type = ErrorCauseArgumentType.LIBRARY_LIMIT_REACHED;
            }
        }
        return type;
    }
}

