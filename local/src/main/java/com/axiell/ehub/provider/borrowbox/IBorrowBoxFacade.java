package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.auth.Patron;

interface IBorrowBoxFacade {

    FormatsDTO getFormats(ContentProviderConsumer contentProviderConsumer, Patron patron, String language, String contentProviderRecordId);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, Patron patron, String language, String contentProviderRecordId, String formatId);

    CheckoutDTO getCheckout(ContentProviderConsumer contentProviderConsumer, Patron patron, String language, String contentProviderLoanId);
}
