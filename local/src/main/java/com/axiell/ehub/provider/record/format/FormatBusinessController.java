/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
import com.axiell.ehub.security.AuthInfo;

/**
 * Default implementation of the {@link IFormatBusinessController}.
 */
public class FormatBusinessController implements IFormatBusinessController {

    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController;

    @Autowired(required = true)
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;

    @Override
    @Transactional(readOnly = true)
    public Formats getFormats(AuthInfo authInfo, String contentProviderName, String contentProviderRecordId, String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final String libraryCard = authInfo.getLibraryCard();
        return contentProviderDataAccessorFacade.getFormats(ehubConsumer, contentProviderName, libraryCard, contentProviderRecordId, language);
    }
}
