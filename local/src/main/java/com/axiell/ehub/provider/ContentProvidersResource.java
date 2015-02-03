/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.security.AuthInfo;
import org.springframework.beans.factory.annotation.Required;

import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.RecordsResource;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;

import javax.xml.ws.Response;

public class ContentProvidersResource implements IContentProvidersResource {
    private final IFormatBusinessController formatBusinessController;
    private final AuthInfo authInfo;

    public ContentProvidersResource(IFormatBusinessController formatBusinessController, AuthInfo authInfo) {
        this.formatBusinessController = formatBusinessController;
        this.authInfo = authInfo;
    }

    @Override
    public ContentProvidersDTO root() {
        throw new NotImplementedException("Root path in ContentProvidersResource has not been implemented yet");
    }

    @Override
    public ContentProviderDTO getContentProvider(String contentProviderAlias) {
        throw new NotImplementedException("Get content provider path in ContentProvidersResource has not been implemented yet");
    }

    @Override
    public IRecordsResource records(String contentProviderAlias) {
        return new RecordsResource(formatBusinessController, authInfo, contentProviderAlias);
    }
}
