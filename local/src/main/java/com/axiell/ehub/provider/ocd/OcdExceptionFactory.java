package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class OcdExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {
    static final String MESSAGE_NO_FULFILLMENT_COPY_AVAILABLE = "No fulfillment copy available.";

    public OcdExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                               final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory,ErrorDTO.class);
    }

    @Override
    protected String getStatus(final ErrorDTO error) {
        return null;
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getMessage();
    }

    @Override
    protected ErrorCauseArgumentValue.Type getErrorCauseArgumentValueType(final String status,final String message) {
        ErrorCauseArgumentValue.Type type = null;
        if (message != null) {
            if (MESSAGE_NO_FULFILLMENT_COPY_AVAILABLE.equals(message)) {
                type = ErrorCauseArgumentValue.Type.LIBRARY_LIMIT_REACHED;
            }
        }
        return type;
    }
}

