package com.axiell.ehub.provider.overdrive;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links.ContentLink;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;

@Component
public class OverDriveDataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired(required = true)
    private IOverDriveFacade overDriveFacade;

    @Override
    public Formats getFormats(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final String language) {
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
	final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
	final FormatTextBundle textBundle = formatDecoration == null ? null : formatDecoration.getTextBundle(language);

	final String name;
	final String description;

	if (textBundle == null) {
	    name = discoveryFormat.getName();
	    description = null;
	} else {
	    name = textBundle.getName() == null ? discoveryFormat.getName() : textBundle.getName();
	    description = textBundle.getDescription();
	}

	return new Format(formatId, name, description, null);
    }

    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin,
	    final PendingLoan pendingLoan) {
	final OAuthAccessToken patronAccessToken = overDriveFacade.getPatronOAuthAccessToken(contentProviderConsumer, libraryCard, pin);
	final String productId = pendingLoan.getContentProviderRecordId();
	final String formatType = pendingLoan.getContentProviderFormatId();
	final Checkout checkout = overDriveFacade.checkout(contentProviderConsumer, patronAccessToken, productId, formatType);
	final Date expirationDate = checkout.getExpirationDate();

	final DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(productId, formatType);
	final DownloadLinkTemplate downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckout(checkout);
	
	if (downloadLinkTemplate == null)
	    throw NotFoundExceptionFactory.create(ContentProviderName.OVERDRIVE, productId, formatType);
	
	final String contentUrl = getContentUrl(contentProviderConsumer, patronAccessToken, downloadLinkTemplate);

	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatType);
	final IContent content = createContent(contentUrl, formatDecoration);
	final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, productId,
		formatDecoration).build();
	return new ContentProviderLoan(metadata, content);
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
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin,
	    final ContentProviderLoanMetadata contentProviderLoanMetadata) {
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