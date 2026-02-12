package com.axiell.ehub.local.provider.record.issue;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.core.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.local.consumer.IConsumerBusinessController;
import com.axiell.ehub.local.provider.IContentProviderDataAccessorFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class IssueBusinessController implements IIssueBusinessController {

    @Autowired
    private IConsumerBusinessController consumerBusinessController;

    @Autowired
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;

    @Override
    @Transactional(readOnly = true)
    public List<Issue> getIssues(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final Patron patron = authInfo.getPatron();
        return contentProviderDataAccessorFacade.getIssues(ehubConsumer, contentProviderName, patron, contentProviderRecordId, language);
    }
}
