package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.auth.Patron;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
import com.axiell.auth.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of the {@link IIssueBusinessController}.
 */
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
