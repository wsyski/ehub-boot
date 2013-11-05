package com.axiell.ehub.provider.overdrive;

import java.util.List;

import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;

class DownloadLinkTemplateFinder {
    private final CirculationFormat providedFormat;

    DownloadLinkTemplateFinder(final String productId, final String formatType) {
	providedFormat = new CirculationFormat(productId, formatType);
    }
    
    DownloadLinkTemplate findFromCheckout(final Checkout checkout) {
	final List<CirculationFormat> formats = checkout.getFormats();

	for (CirculationFormat format : formats) {
	    if (providedFormat.equals(format)) {
		final LinkTemplates linkTemplates = format.getLinkTemplates();
		return linkTemplates.getDownloadLink();
	    }
	}

	return null;
    }
    
    DownloadLinkTemplate findFromCheckouts(final Checkouts checkouts) {
	for (Checkout checkout : checkouts.getCheckouts()) {
	    final DownloadLinkTemplate downloadLinkTemplate = findFromCheckout(checkout);
	    
	    if (downloadLinkTemplate != null)
		return downloadLinkTemplate;
	}
	
	throw NotFoundExceptionFactory.create(ContentProviderName.OVERDRIVE, providedFormat.getReserveId(), providedFormat.getFormatType());
    }
}
