package com.axiell.ehub.provider.elib.elibu;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_KEY;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.SUBSCRIPTION_ID;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.CONSUME_LICENSE_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.PRODUCT_URL;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.resteasy.client.ProxyFactory;
import org.springframework.stereotype.Component;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;

@Component
class ElibUFacade implements IElibUFacade {
    private static final String UTF8 = "UTF-8";

    @Override
    public Response getProduct(ContentProviderConsumer contentProviderConsumer, String elibuRecordId) {
	final String serviceId = contentProviderConsumer.getProperty(ELIBU_SERVICE_ID);
        final String md5ServiceKey = getMd5ServiceKey(contentProviderConsumer);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String productUrl = contentProvider.getProperty(PRODUCT_URL);
        final IElibUResource elibUResource = ProxyFactory.create(IElibUResource.class, productUrl);
        return elibUResource.getProduct(serviceId, md5ServiceKey, elibuRecordId);
    }

    private String getMd5ServiceKey(ContentProviderConsumer contentProviderConsumer) {
	final String serviceKey = contentProviderConsumer.getProperty(ELIBU_SERVICE_KEY);
        return md5Hex(serviceKey.getBytes());
    }
    
    @Override
    public Response consumeLicense(ContentProviderConsumer contentProviderConsumer, String libraryCard) {
	final String serviceId = contentProviderConsumer.getProperty(ELIBU_SERVICE_ID);
        final String md5ServiceKey = getMd5ServiceKey(contentProviderConsumer);
        final String subscriptionId = contentProviderConsumer.getProperty(SUBSCRIPTION_ID);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String consumeLicenseUrl = contentProvider.getProperty(CONSUME_LICENSE_URL);
        final IElibUResource elibUResource = ProxyFactory.create(IElibUResource.class, consumeLicenseUrl);
        return elibUResource.consumeLicense(serviceId, md5ServiceKey, subscriptionId, libraryCard);
    }
    
    @Override
    public Response consumeProduct(ContentProviderConsumer contentProviderConsumer, Integer licenseId, String elibuRecordId) {
	final String serviceId = contentProviderConsumer.getProperty(ELIBU_SERVICE_ID);
        final String serviceKey = contentProviderConsumer.getProperty(ELIBU_SERVICE_KEY);
        final String md5ServiceKey = makeMd5ServiceKey(serviceKey);
        final String checksum = new StringBuilder(serviceId).append(serviceKey).append(licenseId).append(elibuRecordId).toString();
        final String md5Checksum = DigestUtils.md5Hex(checksum.getBytes());
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String consumeProductUrl = contentProvider.getProperty(PRODUCT_URL);
        final IElibUResource elibUResource = ProxyFactory.create(IElibUResource.class, consumeProductUrl);
        return elibUResource.consumeProduct(serviceId, md5ServiceKey, elibuRecordId, licenseId, md5Checksum);
    }

    private String makeMd5ServiceKey(final String serviceKey) {
	byte[] serviceKeyAsBytes = toByteArray(serviceKey);
	return DigestUtils.md5Hex(serviceKeyAsBytes);
    }

    private byte[] toByteArray(final String serviceKey) {
	try {
	    return serviceKey.getBytes(UTF8);    
	} catch (UnsupportedEncodingException e) {
	    throw new InternalServerErrorException("Could not get service key in '" + UTF8 + "' encoding", e);
	}
    }
}
