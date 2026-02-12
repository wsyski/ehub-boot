package com.axiell.ehub.local.provider;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.loan.EhubLoan;
import com.axiell.ehub.local.loan.PendingLoan;

import java.util.List;

public interface IContentProviderDataAccessorFacade {

    List<Issue> getIssues(EhubConsumer ehubConsumer, String contentProviderAlias, Patron patron, String contentProviderRecordId, String language);

    ContentProviderLoan createLoan(EhubConsumer ehubConsumer, Patron patron, PendingLoan pendingLoan, String language);

    Content getContent(EhubConsumer ehubConsumer, EhubLoan ehubLoan, FormatDecoration formatDecoration, Patron patron, String language);

    ContentProviderConsumer getContentProviderConsumer(EhubConsumer ehubConsumer, String contentProviderAlias);
}
