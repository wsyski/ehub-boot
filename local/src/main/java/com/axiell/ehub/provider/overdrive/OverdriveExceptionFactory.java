package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class OverdriveExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {

    public OverdriveExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                     final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory,ErrorDTO.class);
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
            if ("NotFound".equals(status)) {
                type = ErrorCauseArgumentValue.Type.PRODUCT_UNAVAILABLE;
            }
        }
        return type;
    }
}

