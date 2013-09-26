package com.axiell.ehub.provider.publit;

import java.util.List;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.publit.api.Product;
import com.axiell.ehub.provider.publit.api.ShopCustomerOrder;
import com.axiell.ehub.provider.publit.api.ShopOrderUrl;

interface IPublitFacade {

    List<Product> getProduct(ContentProviderConsumer contentProviderConsumer, String publitRecordId);
    
    ShopCustomerOrder createShopOrder(ContentProviderConsumer contentProviderConsumer, String publitRecordId, String libraryCard);
    
    ShopOrderUrl getShopOrderUrl(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId);
}
