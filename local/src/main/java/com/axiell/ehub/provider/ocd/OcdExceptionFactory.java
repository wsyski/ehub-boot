package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class OcdExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO> implements IContentProviderExceptionFactory<ErrorDTO> {
    static final String MESSAGE_NO_FULFILLMENT_COPY_AVAILABLE = "No fulfillment copy available";
    static final String MESSAGE_PATRON_ID_AND_TITLE_ID_INVALID = "PatronId and TitleId are invalid";
    static final String MESSAGE_CHECKOUT_LIMIT_REACHED = "Checkout limit of (";

    public OcdExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                               final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory,ErrorDTO.class);
    }

    @Override
    protected String getCode(final ErrorDTO error) {
        return null;
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getMessage();
    }

    @Override
    protected ErrorCauseArgumentType getErrorCauseArgumentValueType(final String code, final String message) {
        ErrorCauseArgumentType type = null;
        if (message != null) {
            if (message.contains(MESSAGE_NO_FULFILLMENT_COPY_AVAILABLE)) {
                type = ErrorCauseArgumentType.LIBRARY_LIMIT_REACHED;
            } else if (message.contains(MESSAGE_PATRON_ID_AND_TITLE_ID_INVALID)) {
                type = ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;
            } else if (message.contains(MESSAGE_CHECKOUT_LIMIT_REACHED)) {
                type = ErrorCauseArgumentType.MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED;
            }
        }
        return type;
    }
}

