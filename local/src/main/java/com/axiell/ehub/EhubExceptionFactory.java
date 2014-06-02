package com.axiell.ehub;

import com.axiell.ehub.provider.ContentProviderName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.ErrorCause.CONTENT_PROVIDER_ERROR;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;

@Component
public class EhubExceptionFactory implements IEhubExceptionFactory {
    @Autowired(required = true)
    private IErrorCauseArgumentValueRepository errorCauseArgumentValueRepository;

    @Override
    public InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(final ContentProviderName contentProviderName, final ErrorCauseArgumentValue.Type argValueType, final String language) {
        final ErrorCauseArgument contentProviderNameArg = new ErrorCauseArgument(CONTENT_PROVIDER_NAME, contentProviderName);
        final ErrorCauseArgument statusArg = new ErrorCauseArgumentBuilder(errorCauseArgumentValueRepository, CONTENT_PROVIDER_STATUS, argValueType).language(language).build();
        return new InternalServerErrorException(CONTENT_PROVIDER_ERROR, contentProviderNameArg, statusArg);
    }
}
