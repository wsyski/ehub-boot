package com.axiell.ehub.provider.publit;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_PASSWORD;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_USERNAME;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.PRODUCT_URL;

import java.util.Date;
import java.util.List;

import org.jboss.resteasy.client.ClientResponseFailure;
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
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.publit.api.IPublitTradeApi;
import com.axiell.ehub.provider.publit.api.Product;
import com.axiell.ehub.provider.publit.api.ShopCustomerOrder;
import com.axiell.ehub.provider.publit.api.ShopOrderUrl;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;

@Component
public class PublitDataAccessor extends AbstractContentProviderDataAccessor {
    @Override
    public Formats getFormats(final ContentProviderConsumer contentProviderConsumer,
            final String contentProviderRecordId,
            final String language) {
        Formats formats = new Formats();

        try {
            final IPublitTradeApi api = getProductApi(contentProviderConsumer);
            List<Product> products = api.getProduct(contentProviderRecordId);
            for (Product product : products) {
                formats.addFormat(new Format(product.getType(), product.getType(), null, null));
            }
        } catch (ClientResponseFailure failure) {
            throwException(failure);
        }

        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer,
            final String libraryCard,
            final String pin,
            final PendingLoan pendingLoan) {
        try {
            ShopCustomerOrder shopCustomerOrder = getCreateLoanApi(contentProviderConsumer).createShopOrder(libraryCard, "x",
                    "x", "x@x.com", "x", "x", "x", "INVOICE", "loan", pendingLoan.getContentProviderRecordId(), "ok");
            ShopOrderUrl shopOrderUrl = getOrderListApi(contentProviderConsumer).getShopOrderUrl(
                    shopCustomerOrder.getId().toString());
            String contentUrl = shopOrderUrl.getDownloadItems().get(0).getUrl();

            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
            final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(pendingLoan
                    .getContentProviderFormatId());
            final IContent content = createContent(contentUrl, formatDecoration);
            final Date expirationDate = new Date(); // Expiration date not provided
            final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata(shopCustomerOrder.getId()
                    .toString(), contentProvider, expirationDate, formatDecoration);
            return new ContentProviderLoan(metadata, content);
        } catch (ClientResponseFailure failure) {
            throwException(failure);
            return null;
        }
    }

    @Override
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer,
            final String libraryCard,
            final String pin,
            final ContentProviderLoanMetadata contentProviderLoanMetadata) {
        try {
            ShopOrderUrl shopOrderUrl = getOrderListApi(contentProviderConsumer).getShopOrderUrl(
                    contentProviderLoanMetadata.getId());
            String contentUrl = shopOrderUrl.getDownloadItems().get(0).getUrl(); // We assume only one item per order
            return createContent(contentUrl, contentProviderLoanMetadata.getFormatDecoration());
        } catch (ClientResponseFailure failure) {
            throwException(failure);
            return null;
        }
    }

    private void throwException(ClientResponseFailure failure) {
        ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME,
                ContentProviderName.PUBLIT);
        ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS,
                String.valueOf(failure.getResponse().getStatus()));
        String errorMessage = failure.getMessage();

        throw new InternalServerErrorException(errorMessage, ErrorCause.CONTENT_PROVIDER_ERROR,
                argContentProviderName, argContentProviderStatus);
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
