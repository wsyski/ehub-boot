package com.axiell.ehub.core.controller.external.v5_0.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.common.controller.external.v5_0.provider.IRecordsResource;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.RecordDTO;
import com.axiell.ehub.common.provider.record.Record;
import com.axiell.ehub.core.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.common.provider.record.issue.Issue;

import java.util.List;

public final class RecordsResource implements IRecordsResource {
    private final IIssueBusinessController issueBusinessController;
    private final String contentProviderAlias;

    public RecordsResource(final IIssueBusinessController issueBusinessController, final String contentProviderAlias) {
        this.issueBusinessController = issueBusinessController;
        this.contentProviderAlias = contentProviderAlias;
    }

    @Override
    public RecordDTO getRecord(final AuthInfo authInfo, final String contentProviderRecordId, final String language) {
        List<Issue> issues = issueBusinessController.getIssues(authInfo, contentProviderAlias, contentProviderRecordId, language);
        Record record = new Record(contentProviderRecordId, issues);
        return record.toDTO();
    }
}
