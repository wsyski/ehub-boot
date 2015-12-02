package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanRequestResult;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

public interface IAskewsFacade {
    
    LoanRequestResult processLoan(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, Patron patron);

    ArrayOfLoanDetails getLoanDetails(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId, Patron patron);
}
