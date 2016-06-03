package com.axiell.ehub.provider.ep;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractEpDataAccessor<F extends IEpFacade> extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEpDataAccessor.class);

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final RecordDTO formatsDTO = getFacade().getRecord(contentProviderConsumer, patron, contentProviderRecordId);
        final Formats formats = new Formats();
        for (FormatDTO formatDTO : formatsDTO.getFormats()) {
            final Format format = formatFactory.create(contentProvider, formatDTO.getId(), language);
            formats.addFormat(format);
        }
        return formats;
    }

    protected abstract F getFacade();

    protected Content makeContent(final FormatDecoration formatDecoration, final FormatMetadataDTO formatMetadataDTO) {
        final List<String> hrefs = ContentLinks.fromDTO(formatMetadataDTO.getContentLinks()).hrefs();
        final ContentLinks contentLinks = createContentLinks(hrefs, formatDecoration);
        return new Content(contentLinks).supplementLinks(SupplementLinks.fromDTO(formatMetadataDTO.getSupplementLinks()));
    }
}
