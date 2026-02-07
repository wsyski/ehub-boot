package com.axiell.ehub.provider.ep;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EpExceptionFactory extends AbstractContentProviderExceptionFactory<ErrorDTO>
        implements IContentProviderExceptionFactory<ErrorDTO> {

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
                log.info("Unknown content provider code: " + code);
            }
        }
        return type;
    }
}
