package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;

interface IElibFacade {

    BookAvailability getBookAvailability(ContentProviderConsumer contentProviderConsumer, String elibProductId, Patron patron);

    Product getProduct(ContentProviderConsumer contentProviderConsumer, String elibProductId);

    CreatedLoan createLoan(ContentProviderConsumer contentProviderConsumer, String elibProductId, Patron patron);

    LoanDTO getLoan(ContentProviderConsumer contentProviderConsumer, String loanId);

    GetLoansResponse getLoans(ContentProviderConsumer contentProviderConsumer, Patron patron);

    LibraryProduct getLibraryProduct(ContentProviderConsumer contentProviderConsumer, String elibProductId);
}
