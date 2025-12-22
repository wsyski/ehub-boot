package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;

@Component
public class EhubExceptionFactoryStub extends AbstractEhubExceptionFactory implements IEhubExceptionFactory {

    @Override
    protected ErrorCauseArgument makeStatusArg(final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentType argValueType,
                                               final String language) {
        return new ErrorCauseArgument(CONTENT_PROVIDER_STATUS, argValueType.name());
    }

}
