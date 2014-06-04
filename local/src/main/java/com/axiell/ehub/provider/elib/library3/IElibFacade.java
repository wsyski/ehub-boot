package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;

interface IElibFacade {

    BookAvailability getBookAvailability(ContentProviderConsumer contentProviderConsumer, String elibProductId, String libraryCard);

    Product getProduct(ContentProviderConsumer contentProviderConsumer, String elibProductId);

    CreatedLoan createLoan(ContentProviderConsumer contentProviderConsumer, String elibProductId, String libraryCard);

    Loan getLoan(ContentProviderConsumer contentProviderConsumer, String loanId);

    LibraryProduct getLibraryProduct(ContentProviderConsumer contentProviderConsumer, String elibProductId);
}
