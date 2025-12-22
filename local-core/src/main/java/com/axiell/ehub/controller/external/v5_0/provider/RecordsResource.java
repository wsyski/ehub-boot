package com.axiell.ehub.controller.external.v5_0.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.controller.external.v5_0.provider.dto.RecordDTO;
import com.axiell.ehub.controller.external.v5_0.provider.dto.RecordsDTO;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.provider.record.issue.Issue;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class RecordsResource implements IRecordsResource {
    private final IIssueBusinessController issueBusinessController;
    private final String contentProviderAlias;

    public RecordsResource(final IIssueBusinessController issueBusinessController, final String contentProviderAlias) {
        this.issueBusinessController = issueBusinessController;
        this.contentProviderAlias = contentProviderAlias;
    }

    @Override
    public RecordsDTO root() {
        throw new NotImplementedException("Root path in RecordsResource has not been implemented yet");
    }

    @Override
    public RecordDTO getRecord(final AuthInfo authInfo, final String contentProviderRecordId, final String language) {
        List<Issue> issues = issueBusinessController.getIssues(authInfo, contentProviderAlias, contentProviderRecordId, language);
        Record record = new Record(contentProviderRecordId, issues);
        return record.toDTO();
    }
}
