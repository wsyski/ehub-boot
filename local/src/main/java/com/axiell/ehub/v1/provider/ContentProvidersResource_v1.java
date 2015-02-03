/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.provider;

import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.v1.provider.record.IRecordsResource_v1;
import com.axiell.ehub.v1.provider.record.RecordsResource_v1;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the {@link com.axiell.ehub.provider.IContentProvidersResource}.
 */
public final class ContentProvidersResource_v1 implements IContentProvidersResource_v1 {
    private IFormatBusinessController formatBusinessController;

    @Override
    public IRecordsResource_v1 getRecords(String contentProviderName) {
        return new RecordsResource_v1(formatBusinessController, contentProviderName);
    }

    @Required
    public void setFormatBusinessController(IFormatBusinessController contentProviderBusinessController) {
        this.formatBusinessController = contentProviderBusinessController;
    }
}
