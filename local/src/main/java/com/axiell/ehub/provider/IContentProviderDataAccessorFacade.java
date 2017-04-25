package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.auth.Patron;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.issue.Issue;

import java.util.List;

public interface IContentProviderDataAccessorFacade {

    List<Issue> getIssues(EhubConsumer ehubConsumer, String contentProviderAlias, Patron patron, String contentProviderRecordId, String language);

    ContentProviderLoan createLoan(EhubConsumer ehubConsumer, Patron patron, PendingLoan pendingLoan, String language);

    Content getContent(EhubConsumer ehubConsumer, EhubLoan ehubLoan, FormatDecoration formatDecoration, Patron patron, String language);

    ContentProviderConsumer getContentProviderConsumer(EhubConsumer ehubConsumer, String contentProviderAlias);
}
