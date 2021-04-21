package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.ILmsDataAccessor;
import com.axiell.ehub.lms.ILmsDataAccessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
class OcdFormatHandler implements IOcdFormatHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OcdFormatHandler.class);

    @Autowired
    private ILmsDataAccessorFactory lmsDataAccessorFactory;

    @Override
    public String getContentProviderFormat(final ContentProviderConsumer contentProviderConsumer, final String contentProviderAlias,
                                           final String contentProviderRecordId) {
        final EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        final ILmsDataAccessor lmsDataAccessor = lmsDataAccessorFactory.getLmsDataAccessor(ehubConsumer);
        final String mediaClass = lmsDataAccessor.getMediaClass(ehubConsumer, contentProviderAlias, contentProviderRecordId, Locale.getDefault());
        if ("eAudio".equals(mediaClass) || "eBook".equals(mediaClass)) {
            return mediaClass;
        } else {
            LOGGER.error("Unexpected media class: " + mediaClass + " for record: " + contentProviderRecordId + " provider: " + contentProviderAlias);
            return null;
        }
    }
}
