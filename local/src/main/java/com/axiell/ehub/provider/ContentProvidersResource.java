/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import org.springframework.beans.factory.annotation.Required;

import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.RecordsResource;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;

/**
 * Default implementation of the {@link IContentProvidersResource}.
 */
public final class ContentProvidersResource implements IContentProvidersResource {
    private IFormatBusinessController formatBusinessController;
    
    /**
     * @see com.axiell.ehub.contentprovider.IContentProvidersResource#getRecords(java.lang.String)
     */
    @Override
    public IRecordsResource getRecords(String contentProviderName) {
        return new RecordsResource(formatBusinessController, contentProviderName);
    }
    
    /**
     * Sets the {@link IFormatBusinessController}.
     *
     * @param contentProviderBusinessController the {@link IContentProviderBusinessController} to set
     */
    @Required
    public void setFormatBusinessController(IFormatBusinessController contentProviderBusinessController) {
        this.formatBusinessController = contentProviderBusinessController;
    }
}
