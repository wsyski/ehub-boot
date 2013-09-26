package com.axiell.ehub.provider.elib.library;

import com.axiell.ehub.consumer.ContentProviderConsumer;

interface IElibFacade {

    se.elib.library.product.Response getProduct(ContentProviderConsumer contentProviderConsumer, String elibRecordId, String language);
    
    se.elib.library.loan.Response createLoan(ContentProviderConsumer contentProviderConsumer, String elibRecordId, String formatId, String libraryCard, String pin);
    
    se.elib.library.orderlist.Response getOrderList(ContentProviderConsumer contentProviderConsumer, String libraryCard);
}
