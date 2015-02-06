package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.record.format.Formats;

public interface IContentProviderDataAccessorFacade {
    
    Formats getFormats(EhubConsumer ehubConsumer, String contentProviderAlias, Patron patron, String contentProviderRecordId, String language);

    ContentProviderLoan createLoan(EhubConsumer ehubConsumer, Patron patron, PendingLoan pendingLoan, String language);

    ContentLink getContent(EhubConsumer ehubConsumer, EhubLoan ehubLoan, Patron patron, String language);
}
