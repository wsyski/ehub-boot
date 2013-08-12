package com.axiell.ehub.provider.publit;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.publit.api.IPublitTradeApi;
import com.axiell.ehub.provider.publit.api.Product;
import com.axiell.ehub.provider.publit.api.ShopOrderUrl;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.Formats;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublitDataAccessor extends AbstractContentProviderDataAccessor {
    @Override
    public Formats getFormats(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final String language) {
        final IPublitTradeApi api = getApi();
        final List<Product> products = api.getProduct(contentProviderRecordId);
        System.out.println("products = " + products);
        Formats formats = new Formats();
        for (Product product : products) {
            formats.addFormat(new Format());
        }

    }

    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin, final PendingLoan pendingLoan) {
        getApi().createShopOrder()
    }

    @Override
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin, final ContentProviderLoanMetadata contentProviderLoanMetadata) {
        final ShopOrderUrl shopOrderUrl = getApi().getShopOrderUrl(contentProviderLoanMetadata.getId());
        return null;
    }

    private IPublitTradeApi getApi() {
       return PublitTradeApiFactory.getApi("user", "pass", "http://endpoint");
    }

    @Override
    public ContentProviderName getContentProviderName() {
        return ContentProviderName.PUBLIT;
    }
}
