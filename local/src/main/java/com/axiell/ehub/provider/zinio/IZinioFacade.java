package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.authinfo.Patron;

import java.util.List;

interface IZinioFacade {

    String login(ContentProviderConsumer contentProviderConsumer, Patron patron, String language);

    void checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String issueId, String language);

    List<IssueDTO> getIssues(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, String language);

    String getContentUrl(String loginUrl, String issueId);

}
