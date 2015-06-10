package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.CREATE_LOAN_FAILED;

@Component
public class OcdDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OcdDataAccessor.class);
    @Autowired
    private IOcdAuthenticator ocdAuthenticator;
    @Autowired
    private IPalmaDataAccessor palmaDataAccessor;
    @Autowired
    private IFormatFactory formatFactory;
    @Autowired
    private IOcdCheckoutHandler ocdCheckoutHandler;
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderAlias = data.getContentProviderAlias();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        final String mediaClass = palmaDataAccessor.getMediaClass(ehubConsumer, contentProviderAlias, contentProviderRecordId);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final Formats formats = new Formats();
        if (!StringUtils.isBlank(mediaClass)) {
            final Format format = formatFactory.create(contentProvider, mediaClass, language);
            formats.addFormat(format);
        }
        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final BearerToken bearerToken = ocdAuthenticator.authenticate(data);
        final Checkout checkout = ocdCheckoutHandler.checkout(bearerToken, data);
        if (checkout.isSuccessful()) {
            final String contentProviderLoanId = checkout.getTransactionId();
            final Checkout completeCheckout = ocdCheckoutHandler.getCompleteCheckout(bearerToken, data, contentProviderLoanId);
            final ContentProviderLoanMetadata loanMetadata = makeContentProviderLoanMetadata(data, completeCheckout);
            final ContentLink contentLink = makeContent(loanMetadata, checkout);
            return new ContentProviderLoan(loanMetadata, contentLink);
        }
        throw makeCreateLoanFailedException(data);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(final CommandData data, final Checkout checkout) {
        final Date expirationDate = checkout.getExpirationDate();
        final String loanId = checkout.getTransactionId();
        return newContentProviderLoanMetadataBuilder(data, expirationDate).contentProviderLoanId(loanId).build();
    }

    private ContentLink makeContent(final ContentProviderLoanMetadata loanMetadata, final Checkout checkout) {
        final String contentUrl = checkout.getDownloadUrl();
        final FormatDecoration formatDecoration = loanMetadata.getFormatDecoration();
        return createContent(contentUrl, formatDecoration);
    }

    private InternalServerErrorException makeCreateLoanFailedException(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        return ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, CREATE_LOAN_FAILED, language);
    }

    @Override
    public ContentLink getContent(final CommandData data) {
        final BearerToken bearerToken = ocdAuthenticator.authenticate(data);
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final String contentProviderLoanId = loanMetadata.getId();
        final Checkout checkout = ocdCheckoutHandler.getCompleteCheckout(bearerToken, data, contentProviderLoanId);
        return makeContent(loanMetadata, checkout);
    }
}
