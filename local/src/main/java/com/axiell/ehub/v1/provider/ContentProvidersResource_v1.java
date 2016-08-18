/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.provider;

import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.v1.provider.record.IRecordsResource_v1;
import com.axiell.ehub.v1.provider.record.RecordsResource_v1;
import org.springframework.beans.factory.annotation.Required;

public final class ContentProvidersResource_v1 implements IContentProvidersResource_v1 {
    private IIssueBusinessController issueBusinessController;

    @Override
    public IRecordsResource_v1 getRecords(String contentProviderName) {
        return new RecordsResource_v1(issueBusinessController, contentProviderName);
    }

    @Required
    public void setIssueBusinessController(IIssueBusinessController contentProviderBusinessController) {
        this.issueBusinessController = contentProviderBusinessController;
    }
}
