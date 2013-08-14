/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderDataAccessor;
import com.axiell.ehub.provider.IContentProviderDataAccessorFactory;
import com.axiell.ehub.security.AuthInfo;

/**
 * Default implementation of the {@link IFormatBusinessController}.
 */
public class FormatBusinessController implements IFormatBusinessController {

    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController;

    @Autowired(required = true)
    private IContentProviderDataAccessorFactory contentProviderDataAccessorFactory;

    /**
     * @see com.axiell.ehub.provider.IContentProviderBusinessController#getFormats(com.axiell.ehub.security.AuthInfo,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Formats getFormats(AuthInfo authInfo,
            String contentProviderName,
            String contentProviderRecordId,
            String language) {
        final Long ehubConsumerId = authInfo.getEhubConsumerId();
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        final ContentProviderName contentProviderNameEnum = ContentProviderName.fromString(contentProviderName);
        final ContentProviderConsumer contentProviderConsumer = ehubConsumer
                .getContentProviderConsumer(contentProviderNameEnum);
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory
                .getInstance(contentProviderNameEnum);
        return dataAccessor.getFormats(contentProviderConsumer, contentProviderRecordId, language);
    }
}
