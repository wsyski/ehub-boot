package com.axiell.ehub.provider.ep;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EpExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EpExceptionFactory.class);

    public EpExceptionFactory(final ContentProviderConsumer contentProviderConsumer, final String language,
                              final IEhubExceptionFactory ehubExceptionFactory) {
        super(contentProviderConsumer, language, ehubExceptionFactory, ErrorDTO.class);
    }

    @Override
    protected String getCode(final ErrorDTO error) {
        return error == null ? null : error.getCode();
    }

    @Override
    protected String getMessage(final ErrorDTO error) {
        return error == null ? null : error.getMessage();
    }

    @Override
    protected ErrorCauseArgumentType getErrorCauseArgumentValueType(final String code, final String message) {
        ErrorCauseArgumentType type = null;
        if (code != null) {
            try {
                type = ErrorCauseArgumentType.valueOf(code);
            } catch (IllegalArgumentException ex) {
                LOGGER.info("Unknown content provider code: " + code);
            }
        }
        return type;
    }
}
