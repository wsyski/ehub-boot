package com.axiell.ehub.provider.record;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
