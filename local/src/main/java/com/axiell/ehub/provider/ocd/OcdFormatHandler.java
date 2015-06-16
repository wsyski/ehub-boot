package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class OcdFormatHandler implements IOcdFormatHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OcdFormatHandler.class);

    @Autowired
    private IPalmaDataAccessor palmaDataAccessor;

    @Override
    public String getContentProviderFormat(final ContentProviderConsumer contentProviderConsumer, final String contentProviderAlias,
                                           final String contentProviderRecordId) {
        final EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        final String mediaClass = palmaDataAccessor.getMediaClass(ehubConsumer, contentProviderAlias, contentProviderRecordId);
        if ("eAudio".equals(mediaClass) || "eBook".equals(mediaClass)) {
            return mediaClass;
        } else {
            LOGGER.error("Unexpected media class: " + mediaClass + " for record: " + contentProviderRecordId + " provider: " + contentProviderAlias);
            return null;
        }
    }
}
