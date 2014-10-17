package com.axiell.ehub.provider.publit;

import java.util.List;

import com.axiell.ehub.consumer.ContentProviderConsumer;

interface IPublitFacade {

    List<Product> getProduct(ContentProviderConsumer contentProviderConsumer, String publitRecordId);
    
    ShopCustomerOrder createShopOrder(ContentProviderConsumer contentProviderConsumer, String publitRecordId, String patronId);
    
    ShopOrderUrl getShopOrderUrl(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId);
}
