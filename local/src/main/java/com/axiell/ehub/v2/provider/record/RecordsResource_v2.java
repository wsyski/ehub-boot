package com.axiell.ehub.v2.provider.record;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.security.AuthInfo;

import java.util.List;

public final class RecordsResource_v2 implements IRecordsResource_v2 {
    private final IIssueBusinessController issueBusinessController;
    private final String contentProviderAlias;

    public RecordsResource_v2(final IIssueBusinessController issueBusinessController, final String contentProviderAlias) {
        this.issueBusinessController = issueBusinessController;
        this.contentProviderAlias = contentProviderAlias;
    }

    @Override
    public RecordsDTO_v2 root() {
        throw new NotImplementedException("Root path in RecordsResource has not been implemented yet");
    }

    @Override
    public RecordDTO_v2 getRecord(final AuthInfo authInfo, final String contentProviderRecordId, final String language) {
        List<Issue> issues = issueBusinessController.getIssues(authInfo, contentProviderAlias, contentProviderRecordId, language);
        Record record = new Record(contentProviderRecordId, issues);
        return RecordDTOV2Converter.convert(record);
    }
}
