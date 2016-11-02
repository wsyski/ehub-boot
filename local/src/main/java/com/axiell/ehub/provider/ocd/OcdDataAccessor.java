package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.axiell.ehub.provider.record.issue.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class OcdDataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired
    private IOcdFormatHandler ocdFormatHandler;
    @Autowired
    private IFormatFactory formatFactory;
    @Autowired
    private IOcdCheckoutHandler ocdCheckoutHandler;

    @Override
    public List<Issue> getIssues(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderAlias = data.getContentProviderAlias();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String contentProviderFormat = ocdFormatHandler.getContentProviderFormat(contentProviderConsumer, contentProviderAlias, contentProviderRecordId);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final List<Format> formats = new ArrayList<>();
        if (contentProviderFormat != null) {
            final Format format = formatFactory.create(contentProvider, contentProviderFormat, language);
            formats.add(format);
        }
        return Collections.singletonList(new Issue(formats));
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final Checkout checkout = ocdCheckoutHandler.checkout(data);
        final ContentProviderLoanMetadata loanMetadata = makeContentProviderLoanMetadata(data, checkout);
        final Content contentLinks = makeContent(checkout);
        return new ContentProviderLoan(loanMetadata, contentLinks);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(final CommandData data, final Checkout checkout) {
        final Date expirationDate = checkout.getExpirationDate();
        final String loanId = checkout.getTransactionId();
        return newContentProviderLoanMetadataBuilder(data, expirationDate).contentProviderLoanId(loanId).build();
    }

    private Content makeContent(final Checkout checkout) {
        final List<String> contentUrls = checkout.getDownloadUrls();
        final ContentLinks contentLinks = createContentLinks(contentUrls);
        return new Content(contentLinks);
    }

    @Override
    public Content getContent(final CommandData data) {
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final String contentProviderLoanId = loanMetadata.getId();
        final Checkout checkout = ocdCheckoutHandler.getCheckout(data, contentProviderLoanId);
        return makeContent(checkout);
    }
}
