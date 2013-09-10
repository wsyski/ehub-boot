package com.axiell.ehub.loan;

interface IReadyLoanFactory {
    
    ReadyLoan createReadyLoan(EhubLoan ehubLoan, ContentProviderLoan contentProviderLoan);
    
    ReadyLoan createReadyLoan(EhubLoan ehubLoan, IContent content);
}
