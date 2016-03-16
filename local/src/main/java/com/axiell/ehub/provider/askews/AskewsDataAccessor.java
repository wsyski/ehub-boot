package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IExpirationDateFactory;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.axiell.ehub.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class AskewsDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AskewsDataAccessor.class);
    private static final String ASKEWS_FORMAT_ID = "Askews1";
    private static final Integer TITLE_HAS_BEEN_PROCESSED = 4;
    private static final Integer LOAN_SUCCESS = 1;

    @Autowired
    private IAskewsFacade askewsFacade;

    @Autowired
    private IExpirationDateFactory expirationDateFactory;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final Format format = formatFactory.create(contentProvider, ASKEWS_FORMAT_ID, language);
        final Formats formats = new Formats();
        formats.addFormat(format);
        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final Patron patron = data.getPatron();

        final String contentProviderLoanId = processLoan(contentProviderConsumer, contentProviderRecordId, patron);
        final String contentUrl = getContentUrl(contentProviderConsumer, contentProviderLoanId, patron);

        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String formatId = data.getContentProviderFormatId();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);

        final ContentLinks contentLinks = createContentLinks(Collections.singletonList(contentUrl), formatDecoration);
        final Content content = new Content(contentLinks);

        final Date expirationDate = expirationDateFactory.createExpirationDate(contentProvider);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId,
                formatDecoration).contentProviderLoanId(contentProviderLoanId).build();
        return new ContentProviderLoan(metadata, content);
    }

    private String processLoan(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final Patron patron) {
        final LoanRequestResult loanRequestResult = askewsFacade.processLoan(contentProviderConsumer, contentProviderRecordId, patron);

        if (loanRequestWasNotSuccessful(loanRequestResult)) {
            throwInternalServerErrorException(loanRequestResult.getErrorDesc().getValue(), loanRequestResult.getErrorCode());
        }

        return loanRequestResult.getLoanid().toString();
    }

    private boolean loanRequestWasNotSuccessful(final LoanRequestResult loanRequestResult) {
        Integer requestStatus = loanRequestResult.getLoanRequestSuccess();
        return !LOAN_SUCCESS.equals(requestStatus);
    }

    private String getContentUrl(final ContentProviderConsumer contentProviderConsumer, final String contentProviderLoanId, final Patron patron) {
        final LoanDetails loanDetails = getLoanDetails(contentProviderConsumer, contentProviderLoanId, patron);
        return getContentUrl(loanDetails);
    }

    private String getContentUrl(LoanDetails loanDetails) {
        final JAXBElement<String> downloadUrl = loanDetails.getDownloadURL();
        return downloadUrl.getValue();
    }

    private LoanDetails getLoanDetails(final ContentProviderConsumer contentProviderConsumer, final String contentProviderLoanId, final Patron patron) {
        final ArrayOfLoanDetails loanDetailsArray = askewsFacade.getLoanDetails(contentProviderConsumer, contentProviderLoanId, patron);
        final List<LoanDetails> loanDetailsList = loanDetailsArray.getLoanDetails();
        Validate.isNotEmpty(loanDetailsList, "The list of LoanDetails returned from Askews is empty where content provider loan = ID '" + contentProviderLoanId
                + "'");
        final LoanDetails loanDetails = loanDetailsList.get(0);
        validateLoanHasNotFailed(loanDetails);
        return loanDetails;
    }

    private void validateLoanHasNotFailed(LoanDetails loanDetails) {
        if (loanDetails.isHasFailed()) {
            throwInternalServerErrorException(loanDetails.getErrorDesc().getValue(), loanDetails.getErrorCode());
        }
    }

    @Override
    public Content getContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final String contentProviderLoanId = contentProviderLoanMetadata.getId();
        final LoanDetails loanDetail = getLoanDetails(contentProviderConsumer, contentProviderLoanId, data.getPatron());

        if (titleHasNotBeenProcessed(loanDetail)) {
            throwInternalServerErrorException("Title has not yet been processed", loanDetail.getLoanStatus());
        }

        final String contentUrl = getContentUrl(loanDetail);
        final FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
        ContentLinks contentLinks = createContentLinks(Collections.singletonList(contentUrl), formatDecoration);
        return new Content(contentLinks);
    }

    private boolean titleHasNotBeenProcessed(LoanDetails loanDetail) {
        Integer loanStatus = loanDetail.getLoanStatus();
        return !TITLE_HAS_BEEN_PROCESSED.equals(loanStatus);
    }

    private void throwInternalServerErrorException(String errorMessage, Integer errorCode) {
        ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProvider.CONTENT_PROVIDER_ASKEWS);
        ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, String.valueOf(errorCode));
        throw new InternalServerErrorException(errorMessage, ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
    }
}
