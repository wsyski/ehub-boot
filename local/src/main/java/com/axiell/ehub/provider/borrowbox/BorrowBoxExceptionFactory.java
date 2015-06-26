package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class BorrowBoxExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {

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
    protected ErrorCauseArgumentValue.Type getErrorCauseArgumentValueType(final String status,final String message) {
        ErrorCauseArgumentValue.Type type = null;
        if (status != null) {
            if ("notYetRegistered".equals(status)) {
                type = ErrorCauseArgumentValue.Type.INVALID_PATRON;
            } else if ("noCreditsLeft".equals(status)) {
                type = ErrorCauseArgumentValue.Type.BORROWER_LIMIT_REACHED;
            } else if ("notAvailable".equals(status)) {
                type = ErrorCauseArgumentValue.Type.PRODUCT_UNAVAILABLE;
            } else if ("notCopyAvailable".equals(status)) {
                type = ErrorCauseArgumentValue.Type.LIBRARY_LIMIT_REACHED;
            } else if ("alreadyOnLoan".equals(status)) {
                type = ErrorCauseArgumentValue.Type.ALREADY_ON_LOAN;
            }
        }
        return type;
    }
}
