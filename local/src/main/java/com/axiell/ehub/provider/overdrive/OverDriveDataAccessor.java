package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.checkout.Content;
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
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.axiell.ehub.ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;

@Component
public class OverDriveDataAccessor extends AbstractContentProviderDataAccessor {
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private IOverDriveFacade overDriveFacade;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public List<Issue> getIssues(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final OAuthAccessToken oAuthAccessToken = getOAuthAccessToken(data);
        final String productId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final CheckoutDTO checkout = getCheckout(data, oAuthAccessToken);
        if (checkout != null) {
            CirculationFormatsDTO circulationFormats = overDriveFacade.getCirculationFormats(contentProviderConsumer, oAuthAccessToken,
                    checkout.getReserveId());
            return makeIssues(contentProvider, language, circulationFormats.getFormats(), new CirculationFormatExtractor());
        } else {
            final Product product = overDriveFacade.getProduct(contentProviderConsumer, productId);
            if (!product.isAvailable()) {
                throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE,
                        language);
            }
            return makeIssues(contentProvider, language, product.getFormats(), new DiscoveryFormatExtractor());
        }
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
            throw NotFoundExceptionFactory
                    .create(ErrorCause.CONTENT_PROVIDER_RECORD_NOT_FOUND, ContentProvider.CONTENT_PROVIDER_OVERDRIVE, productId, formatType);
        }
        final String contentUrl = getContentUrl(contentProviderConsumer, oAuthAccessToken, downloadLinkTemplate);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatType);
        final ContentLinks contentLinks = createContentLinks(contentUrl);
        final Content content = new Content(contentLinks);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, productId,
                formatDecoration).build();
        return new ContentProviderLoan(metadata, content);
    }

    @Override
    public Content getContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final OAuthAccessToken oAuthAccessToken = getOAuthAccessToken(data);
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = data.getFormatDecoration();
        final CheckoutsDTO checkouts = overDriveFacade.getCheckouts(contentProviderConsumer, oAuthAccessToken);
        final String productId = contentProviderLoanMetadata.getRecordId();
        final String formatType = formatDecoration.getContentProviderFormatId();
        final DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(productId, formatType);
        final DownloadLinkTemplateDTO downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckouts(checkouts);
        final String contentUrl = getContentUrl(contentProviderConsumer, oAuthAccessToken, downloadLinkTemplate);
        final ContentLinks contentLinks = createContentLinks(contentUrl);
        return new Content(contentLinks);
    }

    private <T> List<Issue> makeIssues(final ContentProvider contentProvider, final String language, final Collection<T> discoveryFormats,
                                       final IFormatExtractor<T> formatExtractor) {
        final List<Format> formats = new ArrayList<>();
        for (T discoveryFormat : discoveryFormats) {
            final Format format = formatFactory.create(contentProvider, formatExtractor.getFormatId(discoveryFormat), language);
            formats.add(format);
        }
        return Collections.singletonList(new Issue(formats));
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