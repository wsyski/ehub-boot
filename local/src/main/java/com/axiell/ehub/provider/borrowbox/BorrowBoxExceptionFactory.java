package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class BorrowBoxExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {
    private static final String STATUS_NOT_YET_REGISTERED = "notYetRegistered";
    private static final String STATUS_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_NO_CREDITS_LEFT = "noCreditsLeft";
    private static final String STATUS_NOT_AVAILABLE = "notAvailable";
    private static final String STATUS_NO_COPY_AVAILABLE = "noCopyAvailable";
    private static final String STATUS_ALREADY_ON_LOAN = "alreadyOnLoan";
    private static final String STATUS_PRODUCT_NOT_AVAILABLE_ON_SITE = "productNotAvailableOnSite";

    public BorrowBoxExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
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
            if (STATUS_NOT_YET_REGISTERED.equals(code)) {
                type = ErrorCauseArgumentType.INVALID_PATRON;
            } else if (STATUS_UNAUTHORIZED.equals(code)) {
                type = ErrorCauseArgumentType.INVALID_PATRON;
            } else if (STATUS_NO_CREDITS_LEFT.equals(code)) {
                type = ErrorCauseArgumentType.BORROWER_LIMIT_REACHED;
            } else if (STATUS_NOT_AVAILABLE.equals(code) || STATUS_PRODUCT_NOT_AVAILABLE_ON_SITE.equals(code)) {
                type = ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;
            } else if (STATUS_NO_COPY_AVAILABLE.equals(code)) {
                type = ErrorCauseArgumentType.LIBRARY_LIMIT_REACHED;
            } else if (STATUS_ALREADY_ON_LOAN.equals(code)) {
                type = ErrorCauseArgumentType.ALREADY_ON_LOAN;
            }
        }
        return type;
    }
}
