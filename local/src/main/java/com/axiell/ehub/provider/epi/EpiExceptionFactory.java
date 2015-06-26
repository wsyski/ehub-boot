package com.axiell.ehub.provider.epi;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EpiExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EpiExceptionFactory.class);

    public EpiExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                               final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory, ErrorDTO.class);
    }

    @Override
    protected String getStatus(final ErrorDTO error) {
        return error == null ? null : error.getStatus();
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getMessage();
    }

    @Override
    protected ErrorCauseArgumentValue.Type getErrorCauseArgumentValueType(final String status,final String message) {
        ErrorCauseArgumentValue.Type type = null;
        if (status != null) {
            try {
                type = ErrorCauseArgumentValue.Type.valueOf(status);
            } catch (IllegalArgumentException ex) {
                LOGGER.info("Unknown content provider status: " + status);
            }
        }
        return type;
    }
}
