package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.local.loan.ContentProviderLoan;

import java.util.List;

/**
 * Defines the data accessor interface for the {@link ContentProvider}s.
 */
public interface IContentProviderDataAccessor {

    List<Issue> getIssues(CommandData data);

    ContentProviderLoan createLoan(CommandData data);

    Content getContent(CommandData data);
}
