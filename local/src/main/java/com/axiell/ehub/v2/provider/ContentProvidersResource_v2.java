/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v2.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.ContentProviderDTO;
import com.axiell.ehub.provider.ContentProvidersDTO;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.RecordsResource;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.v2.provider.record.IRecordsResource_v2;
import com.axiell.ehub.v2.provider.record.RecordsResource_v2;

public class ContentProvidersResource_v2 implements IContentProvidersResource_v2 {
    private final IFormatBusinessController formatBusinessController;

    public ContentProvidersResource_v2(IFormatBusinessController formatBusinessController) {
        this.formatBusinessController = formatBusinessController;
    }

    @Override
    public ContentProvidersDTO root() {
        throw new NotImplementedException("Root path in ContentProvidersResource has not been implemented yet");
    }

    @Override
    public ContentProviderDTO getContentProvider(AuthInfo authInfo, String contentProviderAlias) {
        throw new NotImplementedException("Get content provider path in ContentProvidersResource has not been implemented yet");
    }

    @Override
    public IRecordsResource_v2 records(String contentProviderAlias) {
        return new RecordsResource_v2(formatBusinessController, contentProviderAlias);
    }
}
