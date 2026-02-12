package com.axiell.ehub.local.error;

import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.core.error.AbstractEhubExceptionFactory;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.common.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;

@Component
public class EhubExceptionFactory extends AbstractEhubExceptionFactory implements IEhubExceptionFactory {
    @Autowired
    private IErrorCauseArgumentValueRepository errorCauseArgumentValueRepository;

    @Override
    protected ErrorCauseArgument makeStatusArg(final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentType argValueType,
                                               final String language) {
        final String defaultLanguage = contentProviderConsumer.getEhubConsumer().getDefaultLanguage().getId();
        return new ErrorCauseArgumentBuilder(errorCauseArgumentValueRepository, CONTENT_PROVIDER_STATUS, argValueType).language(language).defaultLanguage(
                defaultLanguage).build();
    }
}
