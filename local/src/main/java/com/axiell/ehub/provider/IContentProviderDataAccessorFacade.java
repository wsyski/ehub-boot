package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.record.format.Formats;

public interface IContentProviderDataAccessorFacade {
    
    Formats getFormats(EhubConsumer ehubConsumer, String contentProviderName, String contentProviderRecordId, String language);

    ContentProviderLoan createLoan(EhubConsumer ehubConsumer, String libraryCard, String pin, PendingLoan pendingLoan);

    IContent getContent(EhubConsumer ehubConsumer, EhubLoan ehubLoan, String libraryCard, String pin);
}
