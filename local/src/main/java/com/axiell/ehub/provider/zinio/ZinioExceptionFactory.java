package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class ZinioExceptionFactory extends AbstractContentProviderExceptionFactory<String>
        implements IContentProviderExceptionFactory<String> {
    static final String MESSAGE_NOT_EXISTS = "not exists";
    static final String MESSAGE_UNEXISTED_MAGAZINE_RBID = "unexisted magazine rbid";

    public ZinioExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                 final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory, String.class);
    }

    @Override
    protected String getCode(final String message) {
        return null;
    }

    @Override
    protected String getMessage(final String message) {
        return message;
    }

    @Override
    protected ErrorCauseArgumentType getErrorCauseArgumentValueType(final String code, final String message) {
        ErrorCauseArgumentType type = null;
        if (message != null) {
            if (message.contains(MESSAGE_NOT_EXISTS)) {
                type = ErrorCauseArgumentType.INVALID_PATRON;
            } else if (message.contains(MESSAGE_UNEXISTED_MAGAZINE_RBID)) {
                type = ErrorCauseArgumentType.INVALID_CONTENT_PROVIDER_RECORD_ID;
            }
        }
        return type;
    }
}
