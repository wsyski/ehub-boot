package com.axiell.ehub.local.provider.ep;

import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.local.provider.IContentProviderExceptionFactory;
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
