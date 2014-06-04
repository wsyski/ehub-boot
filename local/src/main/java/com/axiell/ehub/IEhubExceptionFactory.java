package com.axiell.ehub;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProviderName;

public interface IEhubExceptionFactory {

    InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(ContentProviderConsumer contentProviderConsumer,
                                                                                                    ErrorCauseArgumentValue.Type argValueType, String language);
}
