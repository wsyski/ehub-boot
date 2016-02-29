package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO.DownloadLinkTemplateDTO;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links.ContentLink;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.PRODUCT_UNAVAILABLE;

@Component
public class OverDriveDataAccessor extends AbstractContentProviderDataAccessor {
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private IOverDriveFacade overDriveFacade;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final OAuthAccessToken oAuthAccessToken = getOAuthAccessToken(data);
        final String productId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final CheckoutDTO checkout = getCheckout(data, oAuthAccessToken);
        if (checkout != null) {
            CirculationFormatsDTO circulationFormats = overDriveFacade.getCirculationFormats(contentProviderConsumer, oAuthAccessToken,
                    checkout.getReserveId());
            return makeFormats(contentProvider, language, circulationFormats.getFormats(), new CirculationFormatExtractor());
        } else {
            final Product product = overDriveFacade.getProduct(contentProviderConsumer, productId);
            if (!product.isAvailable()) {
                throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE,
                        language);
            }
            return makeFormats(contentProvider, language, product.getFormats(), new DiscoveryFormatExtractor());
        }
    }

    private <T> Formats makeFormats(final ContentProvider contentProvider, final String language, final Collection<T> discoveryFormats,
                                    final IFormatExtractor<T> formatExtractor) {
        final Formats formats = new Formats();
        for (T discoveryFormat : discoveryFormats) {
            final Format format = formatFactory.create(contentProvider, formatExtractor.getFormatId(discoveryFormat), language);
            formats.addFormat(format);
        }
        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final OAuthAccessToken oAuthAccessToken = getOAuthAccessToken(data);
        final String productId = data.getContentProviderRecordId();
        final String formatType = data.getContentProviderFormatId();
        final DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(productId, formatType);
        DownloadLinkTemplateDTO downloadLinkTemplate;
        CheckoutDTO checkout = getCheckout(data, oAuthAccessToken);
        if (checkout != null) {
            CirculationFormatsDTO circulationFormatsDTO = overDriveFacade.getCirculationFormats(contentProviderConsumer, oAuthAccessToken, productId);
            downloadLinkTemplate = downloadLinkTemplateFinder.findFromFormats(circulationFormatsDTO.getFormats());
            if (downloadLinkTemplate == null) {
                CirculationFormatDTO circulationFormatDTO = overDriveFacade.lockFormat(contentProviderConsumer, oAuthAccessToken, productId, formatType);
                downloadLinkTemplate = circulationFormatDTO.getLinkTemplates().getDownloadLink();
            }
        } else {
            checkout = overDriveFacade.checkout(contentProviderConsumer, oAuthAccessToken, productId, formatType);
            downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckout(checkout);
        }
        final Date expirationDate = checkout.getExpirationDate();
        if (downloadLinkTemplate == null) {
            throw NotFoundExceptionFactory.create(ContentProvider.CONTENT_PROVIDER_OVERDRIVE, productId, formatType);
        }
        final String contentUrl = getContentUrl(contentProviderConsumer, oAuthAccessToken, downloadLinkTemplate);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatType);
        final ContentLinks contentLinks = createContent(Collections.singletonList(contentUrl), formatDecoration);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, productId,
                formatDecoration).build();
        return new ContentProviderLoan(metadata, contentLinks);
    }

    @Override
    public ContentLinks getContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final OAuthAccessToken oAuthAccessToken = getOAuthAccessToken(data);
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final CheckoutsDTO checkouts = overDriveFacade.getCheckouts(contentProviderConsumer, oAuthAccessToken);
        final String productId = contentProviderLoanMetadata.getRecordId();
        final FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
        final String formatType = formatDecoration.getContentProviderFormatId();
        final DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(productId, formatType);
        final DownloadLinkTemplateDTO downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckouts(checkouts);
        final String contentUrl = getContentUrl(contentProviderConsumer, oAuthAccessToken, downloadLinkTemplate);
        return createContent(Collections.singletonList(contentUrl), formatDecoration);
    }

    private CheckoutDTO getCheckout(final CommandData data, final OAuthAccessToken oAuthAccessToken) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String productId = data.getContentProviderRecordId();
        final CheckoutsDTO checkouts = overDriveFacade.getCheckouts(contentProviderConsumer, oAuthAccessToken);
        if (checkouts == null) {
            return null;
        }
        final IMatcher<CheckoutDTO> matcher = new CheckoutMatcher(productId);
        try {
            return new CollectionFinder<CheckoutDTO>().find(matcher, checkouts.getCheckouts());
        } catch (IFinder.NotFoundException e) {
            return null;
        }
    }

    private OAuthAccessToken getOAuthAccessToken(final CommandData data) {
        ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final String pin = patron.getPin();
        return overDriveFacade.getPatronOAuthAccessToken(contentProviderConsumer, libraryCard, pin);
    }

    private String getContentUrl(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken,
                                 final DownloadLinkTemplateDTO downloadLinkTemplate) {
        final DownloadLinkDTO downloadLink = overDriveFacade.getDownloadLink(contentProviderConsumer, patronAccessToken, downloadLinkTemplate);
        return getContentUrl(downloadLink);
    }

    private String getContentUrl(final DownloadLinkDTO downloadLink) {
        final Links links = downloadLink.getLinks();
        final ContentLink contentLink = links.getContentLink();
        return contentLink.getHref();
    }

    private interface IFormatExtractor<T> {
        String getFormatId(T object);
    }

    private static class DiscoveryFormatExtractor implements IFormatExtractor<DiscoveryFormatDTO> {

        @Override
        public String getFormatId(final DiscoveryFormatDTO object) {
            return object.getId();
        }
    }

    private static class CirculationFormatExtractor implements IFormatExtractor<CirculationFormatDTO> {

        @Override
        public String getFormatId(final CirculationFormatDTO object) {
            return object.getFormatType();
        }
    }
}