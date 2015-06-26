package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;

public class DefaultContentProviderExceptionFactory extends AbstractContentProviderExceptionFactory<String>
        implements IContentProviderExceptionFactory<String> {


    public DefaultContentProviderExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                                  final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory,String.class);

    }

    @Override
    protected String getStatus(final String error) {
        return null;
    }

    @Override
    protected String getMessage(final String error) {
        return error;
    }

    @Override
    protected ErrorCauseArgumentValue.Type getErrorCauseArgumentValueType(final String status,final String message) {
        return null;
    }
}
