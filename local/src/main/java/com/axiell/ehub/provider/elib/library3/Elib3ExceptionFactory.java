package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;

public class Elib3ExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {


    public Elib3ExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                                 final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory,ErrorDTO.class);

    }

    @Override
    protected String getStatus(final ErrorDTO error) {
        return null;
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getReason();
    }

    @Override
    protected ErrorCauseArgumentValue.Type getErrorCauseArgumentValueType(final String status,final String message) {
        return null;
    }
}
