package com.axiell.ehub.provider.ep;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ep.lpf.CheckoutDTO;
import com.axiell.ehub.provider.ep.lpf.IEpFacade;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class EpDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EpDataAccessor.class);

    @Autowired
    private IEpFacade epFacade;
    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final RecordDTO formatsDTO = epFacade.getRecord(contentProviderConsumer, patron, contentProviderRecordId);
        final Formats formats = new Formats();
        for (FormatDTO formatDTO : formatsDTO.getFormats()) {
            final Format format = formatFactory.create(contentProvider, formatDTO.getId(), language);
            formats.addFormat(format);
        }
        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String contentProviderFormatId = data.getContentProviderFormatId();
        final CheckoutDTO checkoutDTO = epFacade.checkout(contentProviderConsumer, patron, contentProviderRecordId, contentProviderFormatId);
        final ContentProviderLoanMetadata loanMetadata = makeContentProviderLoanMetadata(data, checkoutDTO);
        final Content contentLinks = makeContent(loanMetadata.getFirstFormatDecoration(), checkoutDTO);
        return new ContentProviderLoan(loanMetadata, contentLinks);
    }

    @Override
    public Content getContent(final CommandData data) {
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = data.getFormatDecoration();
        final String contentProviderLoanId = loanMetadata.getId();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final CheckoutDTO checkoutDTO = epFacade.getCheckout(contentProviderConsumer, patron, contentProviderLoanId);
        return makeContent(formatDecoration, checkoutDTO);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(final CommandData data, final CheckoutDTO checkoutDTO) {
        final Date expirationDate = checkoutDTO.getExpirationDate();
        final String loanId = checkoutDTO.getId();
        return newContentProviderLoanMetadataBuilder(data, expirationDate).contentProviderLoanId(loanId).build();
    }

    private Content makeContent(final FormatDecoration formatDecoration, final CheckoutDTO checkoutDTO) {
        final List<String> hrefs = ContentLinks.fromDTO(checkoutDTO.getContentLinks()).hrefs();
        final ContentLinks contentLinks = createContentLinks(hrefs, formatDecoration);
        return new Content(contentLinks).supplementLinks(SupplementLinks.fromDTO(checkoutDTO.getSupplementLinks()));
    }
}
