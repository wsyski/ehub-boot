package com.axiell.ehub;

import com.axiell.ehub.provider.ContentProviderName;

public interface IEhubExceptionFactory {

    InternalServerErrorException createInternalServerErrorExceptionWithContentProviderNameAndStatus(ContentProviderName contentProviderName, ErrorCauseArgumentValue.Type argValueType, String language);
}
