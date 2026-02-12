package com.axiell.ehub.local.provider.elib.library3;

import com.google.common.collect.Lists;

import java.util.List;

import static com.axiell.ehub.local.provider.elib.library3.ElibFormats.EPUB_OFFLINE;
import static com.axiell.ehub.local.provider.elib.library3.ElibFormats.PDF;

/**
 * This class implements the Elib business rule "ignore PDF when ePUB exists". It should be removed when
 * Elib has implemented this business rule.
 */
class FormatFilter {
    private Product.AvailableFormat epubOffline = EPUB_OFFLINE.toAvailableFormat();
    private Product.AvailableFormat pdf = PDF.toAvailableFormat();
    private List<Product.AvailableFormat> filteredFormats;

    public List<Product.AvailableFormat> applyFilter(List<Product.AvailableFormat> formats) {
        if (filteredFormats == null)
            makeFilteredListOfFormats(formats);
        return filteredFormats;
    }

    private void makeFilteredListOfFormats(final List<Product.AvailableFormat> formats) {
        filteredFormats = Lists.newArrayList(formats);
        if (containsBothEpubOfflineAndPdf())
            filteredFormats.remove(pdf);
    }

    private boolean containsBothEpubOfflineAndPdf() {
        return filteredFormats.contains(epubOffline) && filteredFormats.contains(pdf);
    }
}
