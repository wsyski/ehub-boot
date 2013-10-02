package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanRequestResult;
import com.axiell.ehub.consumer.ContentProviderConsumer;

public interface IAskewsFacade {
    
    LoanRequestResult processLoan(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId);

    ArrayOfLoanDetails getLoanDetails(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId);
}
