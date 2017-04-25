package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.auth.Patron;

interface IElibFacade {

    BookAvailability getBookAvailability(ContentProviderConsumer contentProviderConsumer, String elibProductId, Patron patron);

    Product getProduct(ContentProviderConsumer contentProviderConsumer, String elibProductId);

    CreatedLoan createLoan(ContentProviderConsumer contentProviderConsumer, String elibProductId, Patron patron);

    LoanDTO getLoan(ContentProviderConsumer contentProviderConsumer, String loanId);

    GetLoansResponse getLoans(ContentProviderConsumer contentProviderConsumer, Patron patron);

    LibraryProduct getLibraryProduct(ContentProviderConsumer contentProviderConsumer, String elibProductId);
}
