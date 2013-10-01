package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;

public interface IAskewsFacade {
    
    LoanRequestResult processLoan(Integer userId, Integer authId, String contentProviderRecordId, Integer duration, String tokenKey);

    UserLookupResult getUserID(String barcode, Integer authId, String tokenKey);

    ArrayOfLoanDetails getLoanDetails(Integer userId, Integer authId, Integer loanId, String tokenKey);
}
