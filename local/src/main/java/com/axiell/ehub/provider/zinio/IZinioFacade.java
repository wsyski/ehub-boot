package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import java.util.List;

interface IZinioFacade {

    String login(ContentProviderConsumer contentProviderConsumer, Patron patron);

    void checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderIssueId);

    List<IssueDTO> getIssues(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId);

    String getDownloadUrl(String loginUrl, String contentProviderIssueId);

}
