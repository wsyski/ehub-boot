package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProviderName;

public interface IEhubExceptionFactory {

    InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(ContentProviderConsumer contentProviderConsumer,
                                                                                                    ErrorCauseArgumentValue.Type argValueType, String language);
}
