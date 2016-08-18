package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.EhubMessageUtility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IssueBusinessController implements IIssueBusinessController {
    private EhubMessageUtility ehubMessageUtility = new EhubMessageUtility();

    @Override
    public List<Issue> getIssues(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) {
        IssueDTO[] issuesDTO = ehubMessageUtility.getEhubMessage(IssueDTO[].class,"issues",contentProviderName,contentProviderRecordId);
        return Arrays.stream(issuesDTO).map(Issue::new).collect(Collectors.toList());
    }
}
