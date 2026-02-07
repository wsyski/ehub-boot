package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.provider.ContentProvider;

import java.util.List;
import java.util.Map;

class DownloadLinkTemplateFinder {
    private final CirculationFormatDTO providedFormat;

    DownloadLinkTemplateFinder(final String productId, final String formatType) {
        providedFormat = new CirculationFormatDTO(productId, formatType);
    }

    DownloadLinkTemplateDTO findFromCheckout(final CheckoutDTO checkout) {
        final Map<String, DownloadLinkTemplateDTO> links = checkout.getLinks();
        return findFromLinks(links);
    }

    DownloadLinkTemplateDTO findFromLinks(Map<String, DownloadLinkTemplateDTO> links) {
        return links.get("downloadRedirect");
    }

    DownloadLinkTemplateDTO findFromFormats(List<CirculationFormatDTO> formats) {

        for (CirculationFormatDTO format : formats) {
            if (providedFormat.equals(format)) {
                final CirculationFormatDTO.LinkTemplatesDTO linkTemplates = format.getLinkTemplates();
                return linkTemplates.getDownloadLink();
            }
        }
        return null;
    }

    DownloadLinkTemplateDTO findFromCheckouts(final CheckoutsDTO checkouts) {
        for (CheckoutDTO checkout : checkouts.getCheckouts()) {
            final DownloadLinkTemplateDTO downloadLinkTemplate = findFromCheckout(checkout);

            if (downloadLinkTemplate != null)
                return downloadLinkTemplate;
        }
        throw NotFoundExceptionFactory.create(ErrorCause.CONTENT_PROVIDER_RECORD_NOT_CHECKED_OUT, ContentProvider.CONTENT_PROVIDER_OVERDRIVE, providedFormat.getReserveId(), providedFormat.getFormatType());
    }
}
