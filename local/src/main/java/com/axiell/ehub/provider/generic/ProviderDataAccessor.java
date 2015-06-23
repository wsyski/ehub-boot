package com.axiell.ehub.provider.generic;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
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
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProviderDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderDataAccessor.class);

    @Autowired
    private IProviderFacade providerFacade;
    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final FormatsDTO formatsDTO = providerFacade.getFormats(contentProviderConsumer, patron, language, contentProviderRecordId);
        final Formats formats = new Formats();
        for (FormatsDTO.FormatDTO formatDTO : formatsDTO.getFormats()) {
            final Format format = formatFactory.create(contentProvider, formatDTO.getFormatId(), language);
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
        final String language = data.getLanguage();
        final CheckoutDTO checkoutDTO = providerFacade.checkout(contentProviderConsumer, patron, language, contentProviderRecordId, contentProviderFormatId);
        final ContentProviderLoanMetadata loanMetadata = makeContentProviderLoanMetadata(data, checkoutDTO);
        final ContentLink contentLink = makeContent(loanMetadata, checkoutDTO);
        return new ContentProviderLoan(loanMetadata, contentLink);
    }

    @Override
    public ContentLink getContent(final CommandData data) {
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final String contentProviderLoanId = loanMetadata.getId();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String language = data.getLanguage();
        final CheckoutDTO checkoutDTO = providerFacade.getCheckout(contentProviderConsumer, patron, language, contentProviderLoanId);
        return makeContent(loanMetadata, checkoutDTO);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(final CommandData data, final CheckoutDTO checkoutDTO) {
        final Date expirationDate = checkoutDTO.getExpirationDate();
        final String loanId = checkoutDTO.getLoanId();
        return newContentProviderLoanMetadataBuilder(data, expirationDate).contentProviderLoanId(loanId).build();
    }

    private ContentLink makeContent(final ContentProviderLoanMetadata loanMetadata, final CheckoutDTO checkoutDTO) {
        final String contentUrl = checkoutDTO.getContentUrl();
        final FormatDecoration formatDecoration = loanMetadata.getFormatDecoration();
        return createContent(contentUrl, formatDecoration);
    }
}
