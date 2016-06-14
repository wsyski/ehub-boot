package com.axiell.ehub.error;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;

public interface IEhubExceptionFactory {

    InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(ContentProviderConsumer contentProviderConsumer,
                                                                                                    ErrorCauseArgumentType argValueType, String language);

    InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(String message, ContentProviderConsumer contentProviderConsumer,
                                                                                                    ErrorCauseArgumentType argValueType, String language);

    BadRequestException createBadRequestExceptionWithContentProviderNameAndStatus(ContentProviderConsumer contentProviderConsumer,
                                                                                  ErrorCauseArgumentType argValueType, String language);
}
