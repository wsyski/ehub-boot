package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.CREATE_LOAN_FAILED;

@Component
public class OcdDataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired
    private IOcdAuthenticator ocdAuthenticator;
    @Autowired
    private IOcdFormatHandler ocdFormatHandler;
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
        final String contentProviderFormat = ocdFormatHandler.getContentProviderFormat(contentProviderConsumer, contentProviderAlias, contentProviderRecordId);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final Formats formats = new Formats();
        if (contentProviderFormat!=null) {
            final Format format = formatFactory.create(contentProvider, contentProviderFormat, language);
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
            final ContentLink contentLink = makeContent(loanMetadata, completeCheckout);
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
