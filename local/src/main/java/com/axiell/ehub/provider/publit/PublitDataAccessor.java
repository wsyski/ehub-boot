package com.axiell.ehub.provider.publit;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.*;
import com.axiell.ehub.provider.publit.ShopOrderUrl.DownloadItem;
import com.axiell.ehub.provider.record.format.*;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PublitDataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired
    private IPublitFacade publitFacade;

    @Autowired
    private IExpirationDateFactory expirationDateFactory;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final Formats formats = new Formats();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final List<Product> products = getProducts(contentProviderConsumer, data.getContentProviderRecordId());
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();

        for (Product product : products) {
            final Format format = makeFormat(contentProvider, product, data.getLanguage());
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
        final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProvider.CONTENT_PROVIDER_PUBLIT);
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
        return formatFactory.create(contentProvider, formatId, language);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String contentProviderFormatId = data.getContentProviderFormatId();
        final Patron patron = data.getPatron();
        final String patronId = patron.getId();
        final String contentProviderLoanId = createShopOrder(contentProviderConsumer, patronId, contentProviderRecordId);
        final String contentUrl = getContentUrl(contentProviderConsumer, contentProviderLoanId);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(contentProviderFormatId);
        final ContentLink contentLink = createContent(contentUrl, formatDecoration);
        final Date expirationDate = expirationDateFactory.createExpirationDate(contentProvider);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId,
                formatDecoration).contentProviderLoanId(contentProviderLoanId).build();
        return new ContentProviderLoan(metadata, contentLink);
    }

    private String createShopOrder(final ContentProviderConsumer contentProviderConsumer, final String patronId, final String contentProviderRecordId) {
        final ShopCustomerOrder shopCustomerOrder;

        try {
            shopCustomerOrder = publitFacade.createShopOrder(contentProviderConsumer, contentProviderRecordId, patronId);
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
    public ContentLink getContent(final CommandData data) {
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderLoanId = contentProviderLoanMetadata.getId();
        final String contentUrl = getContentUrl(contentProviderConsumer, contentProviderLoanId);
        return createContent(contentUrl, contentProviderLoanMetadata.getFormatDecoration());
    }
}
