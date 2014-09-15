package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.INVALID_CONTENT_PROVIDER_RECORD_ID;
import static com.axiell.ehub.ErrorCauseArgumentValue.Type.INVALID_FORMAT_ID;
import static com.axiell.ehub.ErrorCauseArgumentValue.Type.INVALID_LOAN_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.F1_REGION_ID;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Component
class F1SoapServiceParameterHelper implements IF1SoapServiceParameterHelper {

    @Autowired(required = true)
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public int getMediaId(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final String language) {
        if (isNumeric(contentProviderRecordId))
            return Integer.valueOf(contentProviderRecordId);
        throw ehubExceptionFactory.createBadRequestExceptionWithContentProviderNameAndStatus(contentProviderConsumer, INVALID_CONTENT_PROVIDER_RECORD_ID, language);
    }

    @Override
    public int getRegionId(final ContentProviderConsumer contentProviderConsumer, final String language) {
        final String regionId = contentProviderConsumer.getProperty(F1_REGION_ID);
        if (isNumeric(regionId))
            return Integer.valueOf(regionId);
        throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, INVALID_CONTENT_PROVIDER_RECORD_ID, language);
    }

    @Override
    public int getTypeId(final ContentProviderConsumer contentProviderConsumer, final String formatId, final String language) {
        if (isNumeric(formatId))
            return Integer.valueOf(formatId);
        throw ehubExceptionFactory.createBadRequestExceptionWithContentProviderNameAndStatus(contentProviderConsumer, INVALID_FORMAT_ID, language);
    }

    @Override
    public int getLoanId(ContentProviderConsumer contentProviderConsumer, String loanId, String language) {
        if (isNumeric(loanId))
            return Integer.valueOf(loanId);
        throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, INVALID_LOAN_ID, language);
    }
}
