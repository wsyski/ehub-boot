package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.NotFoundExceptionFactory;
import com.axiell.ehub.common.provider.ContentProvider;

import java.util.List;
import java.util.Map;

public class DownloadLinkTemplateFinder {
    private final CirculationFormatDTO providedFormat;

    public DownloadLinkTemplateFinder(final String productId, final String formatType) {
        providedFormat = new CirculationFormatDTO(productId, formatType);
    }

    public DownloadLinkTemplateDTO findFromCheckout(final CheckoutDTO checkout) {
        final Map<String, DownloadLinkTemplateDTO> links = checkout.getLinks();
        return findFromLinks(links);
    }

    public DownloadLinkTemplateDTO findFromLinks(Map<String, DownloadLinkTemplateDTO> links) {
        return links.get("downloadRedirect");
    }

    public DownloadLinkTemplateDTO findFromFormats(List<CirculationFormatDTO> formats) {

        for (CirculationFormatDTO format : formats) {
            if (providedFormat.equals(format)) {
                final CirculationFormatDTO.LinkTemplatesDTO linkTemplates = format.getLinkTemplates();
                return linkTemplates.getDownloadLink();
            }
        }
        return null;
    }

    public DownloadLinkTemplateDTO findFromCheckouts(final CheckoutsDTO checkouts) {
        for (CheckoutDTO checkout : checkouts.getCheckouts()) {
            final DownloadLinkTemplateDTO downloadLinkTemplate = findFromCheckout(checkout);

            if (downloadLinkTemplate != null)
                return downloadLinkTemplate;
        }
        throw NotFoundExceptionFactory.create(ErrorCause.CONTENT_PROVIDER_RECORD_NOT_CHECKED_OUT, ContentProvider.CONTENT_PROVIDER_OVERDRIVE, providedFormat.getReserveId(), providedFormat.getFormatType());
    }
}
