package com.axiell.ehub.error;

import com.axiell.ehub.*;
import com.axiell.ehub.consumer.ContentProviderConsumer;

import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;

public class EhubExceptionFactoryStub extends AbstractEhubExceptionFactory implements IEhubExceptionFactory {

    @Override
    protected ErrorCauseArgument makeStatusArg(final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentType argValueType,
                                               final String language) {
        return new ErrorCauseArgument(CONTENT_PROVIDER_STATUS, argValueType.name());
    }

}
