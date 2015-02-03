package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
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
import java.util.List;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.CREATE_LOAN_FAILED;

@Component
public class OcdDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OcdDataAccessor.class);
    @Autowired
    private IOcdAuthenticator ocdAuthenticator;
    @Autowired
    private IOcdFacade ocdFacade;
    @Autowired
    private IFormatFactory formatFactory;
    @Autowired
    private IOcdCheckoutHandler ocdCheckoutHandler;
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final List<MediaDTO> allMedia = ocdFacade.getAllMedia(contentProviderConsumer);
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final Formats formats = new Formats();
        try {
            final IMediaMatcher matcher = new ContentProviderRecordIdMediaMatcher(contentProviderRecordId);
            final MediaDTO mediaDTO = MediaFinder.find(matcher, allMedia);
            final String formatId = mediaDTO.getMediaType();
            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
            final String language = data.getLanguage();
            final Format format = formatFactory.create(contentProvider, formatId, language);
            formats.addFormat(format);
        } catch (MediaNotFoundException e) {
            LOGGER.warn("No media found for getContent provider record ID = '" + contentProviderRecordId + "'");
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
            final IContent content = makeContent(loanMetadata, checkout);
            return new ContentProviderLoan(loanMetadata, content);
        }

        throw makeCreateLoanFailedException(data);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(final CommandData data, final Checkout checkout) {
        final Date expirationDate = checkout.getExpirationDate();
        final String loanId = checkout.getTransactionId();
        return newContentProviderLoanMetadataBuilder(data, expirationDate).contentProviderLoanId(loanId).build();
    }

    private IContent makeContent(final ContentProviderLoanMetadata loanMetadata, final Checkout checkout) {
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
    public IContent getContent(final CommandData data) {
        final BearerToken bearerToken = ocdAuthenticator.authenticate(data);
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final String contentProviderLoanId = loanMetadata.getId();
        final Checkout checkout = ocdCheckoutHandler.getCompleteCheckout(bearerToken, data, contentProviderLoanId);
        return makeContent(loanMetadata, checkout);
    }
}
