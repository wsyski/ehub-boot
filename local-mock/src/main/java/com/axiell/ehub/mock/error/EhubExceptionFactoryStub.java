package com.axiell.ehub.mock.error;

import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.core.error.AbstractEhubExceptionFactory;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.common.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;

@Component
public class EhubExceptionFactoryStub extends AbstractEhubExceptionFactory implements IEhubExceptionFactory {

    @Override
    protected ErrorCauseArgument makeStatusArg(final ContentProviderConsumer contentProviderConsumer, final ErrorCauseArgumentType argValueType,
                                               final String language) {
        return new ErrorCauseArgument(CONTENT_PROVIDER_STATUS, argValueType.name());
    }

}
