package com.axiell.ehub.provider.elib.library;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.jboss.resteasy.client.ProxyFactory;
import org.springframework.stereotype.Component;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

@Component
class ElibFacade implements IElibFacade {
    private static final String UTF8 = "UTF-8";
    private static final String ENGLISH = Locale.ENGLISH.getLanguage();
    private static final String CREATE_LOAN_MOBI_POCKET_ID = "X";

    @Override
    public se.elib.library.product.Response getProduct(ContentProviderConsumer contentProviderConsumer, String elibRecordId, String language) {
	final String retailerId = contentProviderConsumer.getProperty(ELIB_RETAILER_ID);
	final String md5RetailerKeyCode = getMd5RetailerKeyCode(contentProviderConsumer);
	final String productUrl = getElibUrl(contentProviderConsumer, ContentProvider.ContentProviderPropertyKey.PRODUCT_URL);
	final IElibProductResource elibProductResource = ProxyFactory.create(IElibProductResource.class, productUrl);
	return elibProductResource.getProduct(retailerId, md5RetailerKeyCode, elibRecordId, language);
    }

    @Override
    public se.elib.library.loan.Response createLoan(ContentProviderConsumer contentProviderConsumer, String elibRecordId, String formatId, String libraryCard,
	    String pin) {
	final String retailerId = contentProviderConsumer.getProperty(ELIB_RETAILER_ID);
	final String md5RetailerKeyCode = getMd5RetailerKeyCode(contentProviderConsumer);
	final String createLoanUrl = getElibUrl(contentProviderConsumer, ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL);
	final IElibLoanResource elibLoanResource = ProxyFactory.create(IElibLoanResource.class, createLoanUrl);
	return elibLoanResource.createLoan(retailerId, md5RetailerKeyCode, elibRecordId, formatId, libraryCard, pin, ENGLISH, CREATE_LOAN_MOBI_POCKET_ID);
    }
    
    @Override
    public se.elib.library.orderlist.Response getOrderList(ContentProviderConsumer contentProviderConsumer, String libraryCard) {
	final String retailerId = contentProviderConsumer.getProperty(ELIB_RETAILER_ID);
        final String md5RetailerKeyCode = getMd5RetailerKeyCode(contentProviderConsumer);
        final String orderListUrl = getElibUrl(contentProviderConsumer, ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL);
        final IElibOrderListResource elibOrderListResource = ProxyFactory.create(IElibOrderListResource.class, orderListUrl);
        return elibOrderListResource.getOrderList(retailerId, md5RetailerKeyCode, libraryCard, ENGLISH);
    }

    private String getMd5RetailerKeyCode(ContentProviderConsumer contentProviderConsumer) {
	final String retailerKeyCode = contentProviderConsumer.getProperty(ELIB_RETAILER_KEY);
	final byte[] retailerKeyCodeAsBytes = toByteArray(retailerKeyCode);
	return md5Hex(retailerKeyCodeAsBytes);
    }
    
    private byte[] toByteArray(final String retailerKeyCode) {
	try {
	    return retailerKeyCode.getBytes(UTF8);    
	} catch (UnsupportedEncodingException e) {
	    throw new InternalServerErrorException("Could not get retailer key code '" + UTF8 + "' encoding", e);
	}
    }
    
    private String getElibUrl(ContentProviderConsumer contentProviderConsumer, ContentProviderPropertyKey propertyKey) {
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        return contentProvider.getProperty(propertyKey);
    }
}
