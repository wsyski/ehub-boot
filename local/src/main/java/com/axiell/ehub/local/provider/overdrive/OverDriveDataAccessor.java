package com.axiell.ehub.local.provider.overdrive;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.NotFoundExceptionFactory;
import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.checkout.ContentLinks;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.common.util.CollectionFinder;
import com.axiell.ehub.common.util.IFinder;
import com.axiell.ehub.common.util.IMatcher;
import com.axiell.ehub.common.util.PatronUtil;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.overdrive.DownloadLinkDTO.Links;
import com.axiell.ehub.local.provider.overdrive.DownloadLinkDTO.Links.ContentLink;
import com.axiell.ehub.local.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.common.ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;

@Component(value = "overDriveDataAccessor")
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
        final String crossRefId = data.getContentProviderRecordId();
        final String formatType = data.getContentProviderFormatId();
        final String language = data.getLanguage();
        final Product product = overDriveFacade.getProduct(contentProviderConsumer, crossRefId, formatType);
        final String productId = product.getId();
        final CheckoutDTO checkout = getCheckout(contentProviderConsumer, productId, oAuthAccessToken);
        if (checkout != null) {
            CirculationFormatsDTO circulationFormats = overDriveFacade.getCirculationFormats(contentProviderConsumer, oAuthAccessToken,
                    checkout.getReserveId());
            return makeIssues(contentProvider, language, circulationFormats.getFormats(), new CirculationFormatExtractor());
        } else {
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
        final String crossRefId = data.getContentProviderRecordId();
        final String formatType = data.getContentProviderFormatId();
        final Product product = overDriveFacade.getProduct(contentProviderConsumer, crossRefId, formatType);
        final String productId = product.getId();
        final DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(productId, formatType);
        DownloadLinkTemplateDTO downloadLinkTemplate;
        CheckoutDTO checkout = getCheckout(contentProviderConsumer, productId, oAuthAccessToken);
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
            throw NotFoundExceptionFactory.create(ErrorCause.CONTENT_PROVIDER_RECORD_NOT_FOUND, ContentProvider.CONTENT_PROVIDER_OVERDRIVE, productId,
                    formatType);
        }
        final String contentUrl = getContentUrl(contentProviderConsumer, oAuthAccessToken, downloadLinkTemplate);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatType);
        final ContentLinks contentLinks = createContentLinks(contentUrl);
        final Content content = new Content(contentLinks);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, crossRefId,
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
        final String crossRefId = contentProviderLoanMetadata.getRecordId();
        final String formatType = formatDecoration.getContentProviderFormatId();
        final Product product = overDriveFacade.getProduct(contentProviderConsumer, crossRefId, formatType);
        final String productId = product.getId();
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

    private CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final String productId, final OAuthAccessToken oAuthAccessToken) {
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
        final String libraryCard = PatronUtil.getMandatoryLibraryCard(patron);
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
