/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library.ElibDataAccessor;
import com.axiell.ehub.provider.publit.PublitDataAccessor;
import com.axiell.ehub.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link IFormatBusinessController}.
 */
public class FormatBusinessController implements IFormatBusinessController {

    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController;

    @Autowired(required = true)
    private ElibDataAccessor elibDataAccessor;

    @Autowired(required = true)
    private ElibUDataAccessor elibUDataAccessor;

    @Autowired(required = true)
    private PublitDataAccessor publitDataAccessor;

    /**
     * @see com.axiell.ehub.provider.IContentProviderBusinessController#getFormats(com.axiell.ehub.security.AuthInfo, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Formats getFormats(AuthInfo authInfo, String contentProviderName, String contentProviderRecordId, String language) {
        final Long ehubConsumerId = authInfo.getEhubConsumerId();
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        final ContentProviderName contentProviderNameEnum = ContentProviderName.fromString(contentProviderName);
        final ContentProviderConsumer contentProviderConsumer = ehubConsumer.getContentProviderConsumer(contentProviderNameEnum);

        switch (contentProviderNameEnum) {
            case ELIB:
                return elibDataAccessor.getFormats(contentProviderConsumer, contentProviderRecordId, language);
            case ELIBU:
                return elibUDataAccessor.getFormats(contentProviderConsumer, contentProviderRecordId, language);
            case PUBLIT:
                return publitDataAccessor.getFormats(contentProviderConsumer, contentProviderRecordId, language);
            default:
                throw new NotImplementedException("Get formats for content provider with name '" + contentProviderNameEnum + "' has not been implemented");
        }
    }
}
