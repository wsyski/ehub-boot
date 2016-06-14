package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;

public class DefaultContentProviderExceptionFactory extends AbstractContentProviderExceptionFactory<String>
        implements IContentProviderExceptionFactory<String> {


    public DefaultContentProviderExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
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
        return null;
    }
}
