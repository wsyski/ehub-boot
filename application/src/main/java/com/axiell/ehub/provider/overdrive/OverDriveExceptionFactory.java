package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class OverDriveExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO> implements IContentProviderExceptionFactory<ErrorDTO> {
    static final String STATUS_UNAUTHORIZED_CLIENT = "unauthorized_client";
    static final String STATUS_NOT_FOUND = "NotFound";
    static final String STATUS_NO_COPIES_AVAILABLE = "NoCopiesAvailable";
    static final String STATUS_PATRON_CHECKOUT_LIMIT = "PatronHasExceededCheckoutLimit";
    static final String STATUS_PATRON_EXCEEDED_CHURNING_LIMIT = "PatronHasExceededChurningLimit";
    static final String STATUS_TITLE_ALREADY_CHECKED_OUT = "TitleAlreadyCheckedOut";
    static final String STATUS_ANOTHER_FORMAT_LOCKED_IN = "AnotherFormatHasBeenLockedIn";


    public OverDriveExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                     final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory, ErrorDTO.class);
    }

    @Override
    protected String getCode(final ErrorDTO error) {
        return error == null ? null : error.getErrorCode();
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getMessage();
    }

    @Override
    protected ErrorCauseArgumentType getErrorCauseArgumentValueType(final String code, final String message) {
        ErrorCauseArgumentType type = null;
        if (code != null) {
            if (STATUS_NOT_FOUND.equals(code)) {
                type = ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;
            } else if (STATUS_UNAUTHORIZED_CLIENT.equals(code)) {
                type = ErrorCauseArgumentType.INVALID_PATRON;
            } else if (STATUS_NO_COPIES_AVAILABLE.equals(code)) {
                type = ErrorCauseArgumentType.LIBRARY_LIMIT_REACHED;
            } else if (STATUS_PATRON_CHECKOUT_LIMIT.equals(code)) {
                type = ErrorCauseArgumentType.BORROWER_LIMIT_REACHED;
            } else if (STATUS_TITLE_ALREADY_CHECKED_OUT.equals(code)) {
                type = ErrorCauseArgumentType.ALREADY_ON_LOAN;
            } else if (STATUS_PATRON_EXCEEDED_CHURNING_LIMIT.equals(code)) {
                type = ErrorCauseArgumentType.BORROWER_LIMIT_REACHED;
            } else if (STATUS_ANOTHER_FORMAT_LOCKED_IN.equals(code)) {
                type = ErrorCauseArgumentType.ANOTHER_FORMAT_LOCKED_IN;
            }
        }
        return type;
    }
}

