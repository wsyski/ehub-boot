package com.axiell.ehub.provider.f1;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IExpirationDateFactory;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.CREATE_LOAN_FAILED;
import static com.axiell.ehub.ErrorCauseArgumentValue.Type.MISSING_CONTENT_IN_LOAN;

@Component
public class F1DataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired(required = true)
    private IF1Facade f1Facade;

    @Autowired(required = true)
    private IFormatFactory formatFactory;

    @Autowired(required = true)
    private IExpirationDateFactory expirationDateFactory;

    @Autowired(required = true)
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final GetFormatResponse getFormatResponse = f1Facade.getFormats(data);
        final Formats formats = new Formats();

        if (getFormatResponse.isValidFormat()) {
            final Format format = makeFormat(data, getFormatResponse);
            formats.addFormat(format);
        }
        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final CreateLoanResponse createLoanResponse = f1Facade.createLoan(data);

        if (createLoanResponse.isValidLoan())
            return makeContentProviderLoan(data, createLoanResponse);
        throw makeCreateLoanFailedException(data, createLoanResponse);
    }

    @Override
    public IContent getContent(final CommandData data) {
        final GetLoanContentResponse getLoanContentResponse = f1Facade.getLoanContent(data);

        if (getLoanContentResponse.isValidContent())
            return makeContentForActiveLoan(data, getLoanContentResponse);
        throw makeMissingContentException(data, getLoanContentResponse);
    }

    private Format makeFormat(CommandData data, GetFormatResponse getFormatResponse) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final String formatId = getFormatResponse.getValue();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        return formatFactory.create(contentProvider, formatId, language);
    }

    private ContentProviderLoan makeContentProviderLoan(final CommandData data, final CreateLoanResponse createLoanResponse) {
        ContentProviderLoanMetadata loanMetadata = populateContentProviderLoanMetadataInCommandData(data, createLoanResponse);
        final GetLoanContentResponse getLoanContentResponse = f1Facade.getLoanContent(data);

        if (getLoanContentResponse.isValidContent())
            return makeContentForNewLoan(loanMetadata, getLoanContentResponse);
        throw makeMissingContentException(data, getLoanContentResponse);
    }

    private ContentProviderLoanMetadata populateContentProviderLoanMetadataInCommandData(CommandData data, CreateLoanResponse createLoanResponse) {
        final String loanId = createLoanResponse.getValue();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final Date expirationDate = expirationDateFactory.createExpirationDate(contentProvider);

        final ContentProviderLoanMetadata metadata = newContentProviderLoanMetadataBuilder(data, expirationDate).contentProviderLoanId(loanId).build();
        data.setContentProviderLoanMetadata(metadata);
        return metadata;
    }

    private IContent makeContentForActiveLoan(CommandData data, GetLoanContentResponse getLoanContentResponse) {
        final String contentUrl = getLoanContentResponse.getValue();
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = loanMetadata.getFormatDecoration();
        return createContent(contentUrl, formatDecoration);
    }

    private ContentProviderLoan makeContentForNewLoan(final ContentProviderLoanMetadata loanMetadata, GetLoanContentResponse getLoanContentResponse) {
        final FormatDecoration formatDecoration = loanMetadata.getFormatDecoration();
        final String contentUrl = getLoanContentResponse.getValue();
        final IContent content = createContent(contentUrl, formatDecoration);
        return new ContentProviderLoan(loanMetadata, content);
    }

    private InternalServerErrorException makeCreateLoanFailedException(final CommandData data, final CreateLoanResponse createLoanResponse) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final String responseValue = createLoanResponse.getValue();
        return ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(responseValue, contentProviderConsumer, CREATE_LOAN_FAILED, language);
    }

    private InternalServerErrorException makeMissingContentException(final CommandData data, final GetLoanContentResponse getLoanContentResponse) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final String responseValue = getLoanContentResponse.getValue();
        return ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(responseValue, contentProviderConsumer, MISSING_CONTENT_IN_LOAN, language);
    }
}
