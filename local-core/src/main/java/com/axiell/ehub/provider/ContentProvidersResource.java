/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.RecordsResource;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public class ContentProvidersResource implements IContentProvidersResource {
    private final IIssueBusinessController issueBusinessController;

    public ContentProvidersResource(IIssueBusinessController issueBusinessController) {
        this.issueBusinessController = issueBusinessController;
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
    public IRecordsResource records(String contentProviderAlias) {
        return new RecordsResource(issueBusinessController, contentProviderAlias);
    }
}
