package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links.ContentLink;
import com.axiell.ehub.provider.record.format.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OverDriveDataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired(required = true)
    private IOverDriveFacade overDriveFacade;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final Product product = overDriveFacade.getProduct(contentProviderConsumer, contentProviderRecordId);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final Formats formats = new Formats();

        for (DiscoveryFormat discoveryFormat : product.getFormats()) {
            final Format format = makeFormat(language, contentProvider, discoveryFormat);
            formats.addFormat(format);
        }

        return formats;
    }

    private Format makeFormat(final String language, final ContentProvider contentProvider, DiscoveryFormat discoveryFormat) {
        final String formatId = discoveryFormat.getId();
        return formatFactory.create(contentProvider, formatId, language);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final String pin = patron.getPin();
        final OAuthAccessToken patronAccessToken = overDriveFacade.getPatronOAuthAccessToken(contentProviderConsumer, libraryCard, pin);
        final String productId = data.getContentProviderRecordId();
        final String formatType = data.getContentProviderFormatId();
        final Checkout checkout = overDriveFacade.checkout(contentProviderConsumer, patronAccessToken, productId, formatType);
        final Date expirationDate = checkout.getExpirationDate();

        final DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(productId, formatType);
        final DownloadLinkTemplate downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckout(checkout);

        if (downloadLinkTemplate == null)
            throw NotFoundExceptionFactory.create(ContentProviderName.OVERDRIVE, productId, formatType);

        final String contentUrl = getContentUrl(contentProviderConsumer, patronAccessToken, downloadLinkTemplate);

        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatType);
        final com.axiell.ehub.checkout.ContentLink contentLink = createContent(contentUrl, formatDecoration);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, productId,
                formatDecoration).build();
        return new ContentProviderLoan(metadata, contentLink);
    }

    private String getContentUrl(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken,
                                 final DownloadLinkTemplate downloadLinkTemplate) {
        final DownloadLink downloadLink = overDriveFacade.getDownloadLink(contentProviderConsumer, patronAccessToken, downloadLinkTemplate);
        return getContentUrl(downloadLink);
    }

    private String getContentUrl(final DownloadLink downloadLink) {
        final Links links = downloadLink.getLinks();
        final ContentLink contentLink = links.getContentLink();
        return contentLink.getHref();
    }

    @Override
    public com.axiell.ehub.checkout.ContentLink getContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final String pin = patron.getPin();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final OAuthAccessToken patronAccessToken = overDriveFacade.getPatronOAuthAccessToken(contentProviderConsumer, libraryCard, pin);
        final Checkouts checkouts = overDriveFacade.getCheckouts(contentProviderConsumer, patronAccessToken);
        final String productId = contentProviderLoanMetadata.getRecordId();
        final FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
        final String formatType = formatDecoration.getContentProviderFormatId();
        final DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(productId, formatType);
        final DownloadLinkTemplate downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckouts(checkouts);
        final String contentUrl = getContentUrl(contentProviderConsumer, patronAccessToken, downloadLinkTemplate);
        return createContent(contentUrl, formatDecoration);
    }
}