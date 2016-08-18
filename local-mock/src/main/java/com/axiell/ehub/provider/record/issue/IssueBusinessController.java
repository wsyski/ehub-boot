package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.EhubMessageUtility;

import java.util.List;

public class IssueBusinessController implements IIssueBusinessController {
    private EhubMessageUtility ehubMessageUtility = new EhubMessageUtility();

    @Override
    public List<Issue> getIssues(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) {
        return ehubMessageUtility.getEhubMessage(List.class,"records",contentProviderName,contentProviderRecordId);
    }
}
