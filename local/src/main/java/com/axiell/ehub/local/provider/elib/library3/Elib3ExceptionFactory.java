package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.local.provider.IContentProviderExceptionFactory;

public class Elib3ExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO> implements IContentProviderExceptionFactory<ErrorDTO> {
    static final String MESSAGE_PRODUCT_UNAVAILABLE = "Product not found";


    public Elib3ExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                 final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory, ErrorDTO.class);

    }

    @Override
    protected String getCode(final ErrorDTO error) {
        return null;
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getReason();
    }

    @Override
    protected ErrorCauseArgumentType getErrorCauseArgumentValueType(final String code, final String message) {
        ErrorCauseArgumentType type = null;
        if (message != null) {
            if (message.contains(MESSAGE_PRODUCT_UNAVAILABLE)) {
                type = ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;
            }
        }
        return type;
    }
}
