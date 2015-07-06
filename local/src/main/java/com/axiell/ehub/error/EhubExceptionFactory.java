package com.axiell.ehub.error;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.ErrorCause.BAD_REQUEST;
import static com.axiell.ehub.ErrorCause.CONTENT_PROVIDER_ERROR;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;

@Component
public class EhubExceptionFactory extends AbstractEhubExceptionFactory implements IEhubExceptionFactory {
    @Autowired
    private IErrorCauseArgumentValueRepository errorCauseArgumentValueRepository;

    @Override
    protected ErrorCauseArgument makeStatusArg(final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentValue.Type argValueType, final String language) {
        final String defaultLanguage = contentProviderConsumer.getEhubConsumer().getDefaultLanguage().getId();
        return new ErrorCauseArgumentBuilder(errorCauseArgumentValueRepository, CONTENT_PROVIDER_STATUS, argValueType).language(language).defaultLanguage(
                defaultLanguage).build();
    }
}
