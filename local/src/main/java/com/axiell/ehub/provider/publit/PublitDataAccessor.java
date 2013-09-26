package com.axiell.ehub.provider.publit;

import java.util.Date;
import java.util.List;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IExpirationDateFactory;
import com.axiell.ehub.provider.publit.api.Product;
import com.axiell.ehub.provider.publit.api.ShopCustomerOrder;
import com.axiell.ehub.provider.publit.api.ShopOrderUrl;
import com.axiell.ehub.provider.publit.api.ShopOrderUrl.DownloadItem;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;

@Component
public class PublitDataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired(required = true)
    private IPublitFacade publitFacade;
    
    @Autowired(required = true)
    private IExpirationDateFactory expirationDateFactory;

    @Override
    public Formats getFormats(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final String language) {
	final Formats formats = new Formats();
	final List<Product> products = getProducts(contentProviderConsumer, contentProviderRecordId);
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();

	for (Product product : products) {
	    final Format format = makeFormat(contentProvider, product, language);
	    formats.addFormat(format);
	}

	return formats;
    }

    private List<Product> getProducts(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId) {
	try {
	    return publitFacade.getProduct(contentProviderConsumer, contentProviderRecordId);
	} catch (ClientResponseFailure failure) {
	    throw makeInternalServerErrorException(failure);
	}
    }

    private InternalServerErrorException makeInternalServerErrorException(final ClientResponseFailure failure) {
	final String status = getStatus(failure);
	final String errorMessage = failure.getMessage();
	return makeInternalServerErrorException(errorMessage, status);
    }

    private InternalServerErrorException makeInternalServerErrorException(final String errorMessage, String status) {
	final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.PUBLIT);
	final ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, status);
	return new InternalServerErrorException(errorMessage, ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
    }

    private String getStatus(final ClientResponseFailure failure) {
	final ClientResponse<?> response = failure.getResponse();
	final int status = response.getStatus();
	return String.valueOf(status);
    }

    private Format makeFormat(final ContentProvider contentProvider, final Product product, final String language) {
	final String formatId = product.getType();
	
	final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
	final FormatTextBundle textBundle = formatDecoration == null ? null : formatDecoration.getTextBundle(language);

	final String name;
	final String description;

	if (textBundle == null) {
	    name = formatId;
	    description = null;
	} else {
	    name = textBundle.getName() == null ? formatId : textBundle.getName();
	    description = textBundle.getDescription();
	}
	
	return new Format(formatId, name, description, null);
    }

    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin,
	    final PendingLoan pendingLoan) {
	final String contentProviderLoanId = createShopOrder(contentProviderConsumer, libraryCard, pendingLoan);
	final String contentUrl = getContentUrl(contentProviderConsumer, contentProviderLoanId);
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(pendingLoan.getContentProviderFormatId());
	final IContent content = createContent(contentUrl, formatDecoration);
	final Date expirationDate = expirationDateFactory.createExpirationDate(contentProvider);
	final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata(contentProviderLoanId, contentProvider, expirationDate, formatDecoration);
	return new ContentProviderLoan(metadata, content);
    }

    private String createShopOrder(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final PendingLoan pendingLoan) {
	final ShopCustomerOrder shopCustomerOrder;
	final String contentProviderRecordId = pendingLoan.getContentProviderRecordId();
	try {
	    shopCustomerOrder = publitFacade.createShopOrder(contentProviderConsumer, contentProviderRecordId, libraryCard);
	} catch (ClientResponseFailure failure) {
	    throw makeInternalServerErrorException(failure);
	}
	return shopCustomerOrder.getId().toString();
    }

    private String getContentUrl(final ContentProviderConsumer contentProviderConsumer, final String contentProviderLoanId) {
	final ShopOrderUrl shopOrderUrl = getShopOrderUrl(contentProviderConsumer, contentProviderLoanId);
	final List<DownloadItem> downloadItems = shopOrderUrl.getDownloadItems();

	if (downloadItems.isEmpty())
	    throw makeInternalServerErrorException("No download items in shop order URL response", "No download items in shop order with ID "
		    + contentProviderLoanId);

	DownloadItem downloadItem = downloadItems.get(0);
	return downloadItem.getUrl();
    }

    private ShopOrderUrl getShopOrderUrl(final ContentProviderConsumer contentProviderConsumer, final String contentProviderLoanId) {
	try {
	    return publitFacade.getShopOrderUrl(contentProviderConsumer, contentProviderLoanId);
	} catch (ClientResponseFailure failure) {
	    throw makeInternalServerErrorException(failure);
	}
    }

    @Override
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin,
	    final ContentProviderLoanMetadata contentProviderLoanMetadata) {
	final String contentProviderLoanId = contentProviderLoanMetadata.getId();
	final String contentUrl = getContentUrl(contentProviderConsumer, contentProviderLoanId);
	return createContent(contentUrl, contentProviderLoanMetadata.getFormatDecoration());
    }
}
