package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.AbstractBusinessController;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.EhubMessageUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IssueBusinessController extends AbstractBusinessController implements IIssueBusinessController {

    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private EhubMessageUtility ehubMessageUtility;

    @Override
    public List<Issue> getIssues(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) {
        Patron patron = authInfo.getPatron();
        IssueDTO[] issuesDTO = ehubMessageUtility.getEhubMessage(IssueDTO[].class, "issues", contentProviderName, contentProviderRecordId,
                patron.getLibraryCard());
        if (issuesDTO == null) {
            ContentProviderConsumer contentProviderConsumer = getContentProviderConsumer(contentProviderName);
            throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer,
                    ErrorCauseArgumentType.PRODUCT_UNAVAILABLE, language);
        }
        return Arrays.stream(issuesDTO).map(Issue::new).collect(Collectors.toList());
    }
}
