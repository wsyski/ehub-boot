package com.axiell.ehub.core.error;

import com.axiell.ehub.common.BadRequestException;
import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.InternalServerErrorException;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;

public interface IEhubExceptionFactory {

    InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(ContentProviderConsumer contentProviderConsumer,
                                                                                                    ErrorCauseArgumentType argValueType, String language);

    InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(String message, ContentProviderConsumer contentProviderConsumer,
                                                                                                    ErrorCauseArgumentType argValueType, String language);

    BadRequestException createBadRequestExceptionWithContentProviderNameAndStatus(ContentProviderConsumer contentProviderConsumer,
                                                                                  ErrorCauseArgumentType argValueType, String language);
}
