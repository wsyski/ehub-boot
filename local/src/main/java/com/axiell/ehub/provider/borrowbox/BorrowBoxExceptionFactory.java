package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.ErrorCauseArgumentValue;
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
    protected String getStatus(final ErrorDTO error) {
        return error == null ? null : error.getErrorCode();
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getMessage();
    }

    @Override
    protected ErrorCauseArgumentValue.Type getErrorCauseArgumentValueType(final String status, final String message) {
        ErrorCauseArgumentValue.Type type = null;
        if (status != null) {
            if (STATUS_NOT_YET_REGISTERED.equals(status)) {
                type = ErrorCauseArgumentValue.Type.INVALID_PATRON;
            } else if (STATUS_UNAUTHORIZED.equals(status)) {
                type = ErrorCauseArgumentValue.Type.INVALID_PATRON;
            } else if (STATUS_NO_CREDITS_LEFT.equals(status)) {
                type = ErrorCauseArgumentValue.Type.BORROWER_LIMIT_REACHED;
            } else if (STATUS_NOT_AVAILABLE.equals(status) || STATUS_PRODUCT_NOT_AVAILABLE_ON_SITE.equals(status)) {
                type = ErrorCauseArgumentValue.Type.PRODUCT_UNAVAILABLE;
            } else if (STATUS_NO_COPY_AVAILABLE.equals(status)) {
                type = ErrorCauseArgumentValue.Type.LIBRARY_LIMIT_REACHED;
            } else if (STATUS_ALREADY_ON_LOAN.equals(status)) {
                type = ErrorCauseArgumentValue.Type.ALREADY_ON_LOAN;
            }
        }
        return type;
    }
}
