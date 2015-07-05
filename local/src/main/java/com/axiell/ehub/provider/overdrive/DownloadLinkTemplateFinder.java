package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO.DownloadLinkTemplateDTO;

import java.util.List;

class DownloadLinkTemplateFinder {
    private final CirculationFormatDTO providedFormat;

    DownloadLinkTemplateFinder(final String productId, final String formatType) {
        providedFormat = new CirculationFormatDTO(productId, formatType);
    }

    DownloadLinkTemplateDTO findFromCheckout(final CheckoutDTO checkout) {
        final List<CirculationFormatDTO> formats = checkout.getFormats();
        return findFromFormats(formats);
    }

    DownloadLinkTemplateDTO findFromFormats(List<CirculationFormatDTO> formats) {

        for (CirculationFormatDTO format : formats) {
            if (providedFormat.equals(format)) {
                final LinkTemplatesDTO linkTemplates = format.getLinkTemplates();
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
        throw NotFoundExceptionFactory.create(ContentProvider.CONTENT_PROVIDER_OVERDRIVE, providedFormat.getReserveId(), providedFormat.getFormatType());
    }
}
