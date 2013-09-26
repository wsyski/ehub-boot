package com.axiell.ehub.provider.publit;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_PASSWORD;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_USERNAME;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.PRODUCT_URL;

import java.util.List;

import org.springframework.stereotype.Component;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

@Component
class PublitFacade implements IPublitFacade {
    private static final String FIRST_NAME = "x";
    private static final String LAST_NAME = "x";
    private static final String EMAIL = "x@x.com";
    private static final String STREET = "x";
    private static final String ZIP_CODE = "x";
    private static final String CITY = "x";
    private static final String PAYMENT_METHOD = "INVOICE";
    private static final String ORDER_TYPE = "loan";
    private static final String CALLBACK = "ok";

    @Override
    public List<Product> getProduct(ContentProviderConsumer contentProviderConsumer, String publitRecordId) {
	final IPublitTradeApi api = getProductApi(contentProviderConsumer);
	return api.getProduct(publitRecordId);
    }

    @Override
    public ShopCustomerOrder createShopOrder(ContentProviderConsumer contentProviderConsumer, String publitRecordId, String libraryCard) {
	final IPublitTradeApi api = getCreateLoanApi(contentProviderConsumer);
	return api.createShopOrder(libraryCard, FIRST_NAME, LAST_NAME, EMAIL, STREET, ZIP_CODE, CITY, PAYMENT_METHOD, ORDER_TYPE, publitRecordId, CALLBACK);
    }

    @Override
    public ShopOrderUrl getShopOrderUrl(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId) {
	final IPublitTradeApi api = getOrderListApi(contentProviderConsumer);
	return api.getShopOrderUrl(contentProviderLoanId);
    }

    private IPublitTradeApi getProductApi(ContentProviderConsumer contentProviderConsumer) {
	return getApi(PRODUCT_URL, contentProviderConsumer);
    }

    private IPublitTradeApi getCreateLoanApi(ContentProviderConsumer contentProviderConsumer) {
	return getApi(CREATE_LOAN_URL, contentProviderConsumer);
    }

    private IPublitTradeApi getOrderListApi(ContentProviderConsumer contentProviderConsumer) {
	return getApi(ORDER_LIST_URL, contentProviderConsumer);
    }

    private IPublitTradeApi getApi(ContentProviderPropertyKey apiUrl, ContentProviderConsumer contentProviderConsumer) {
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final String url = contentProvider.getProperty(apiUrl);
	final String userName = contentProviderConsumer.getProperty(PUBLIT_USERNAME);
	final String password = contentProviderConsumer.getProperty(PUBLIT_PASSWORD);
	return PublitTradeApiFactory.getApi(userName, password, url);
    }
}
