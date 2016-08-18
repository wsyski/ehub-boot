package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.security.AuthInfo;

import java.util.List;

public class IssueBusinessController implements IIssueBusinessController {
    @Override
    public List<Issue> getIssues(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) {
        return null;
    }
}
