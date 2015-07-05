package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.*;
import com.axiell.ehub.provider.record.format.*;
import com.axiell.ehub.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.Date;
import java.util.List;

@Component
public class AskewsDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AskewsDataAccessor.class);
    private static final String ASKEWS_FORMAT_ID = "Askews1";
    private static final Integer WAITING_TO_PROCESS = 1;
    private static final Integer TITLE_HAS_BEEN_PROCESSED = 4;
    private static final Integer LOAN_SUCCESS = 1;
    private static final int MAX_RETRIES = 60;
    private static final Integer RETRY_WAIT_MILLIS = 1000;

    @Autowired(required = true)
    private IAskewsFacade askewsFacade;

    @Autowired(required = true)
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

        final String contentProviderLoanId = processLoan(contentProviderConsumer, contentProviderRecordId);
        final String contentUrl = getContentUrl(contentProviderConsumer, contentProviderLoanId);

        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String formatId = data.getContentProviderFormatId();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);

        final ContentLink contentLink = createContent(contentUrl, formatDecoration);

        final Date expirationDate = expirationDateFactory.createExpirationDate(contentProvider);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId,
                formatDecoration).contentProviderLoanId(contentProviderLoanId).build();
        return new ContentProviderLoan(metadata, contentLink);
    }

    private String processLoan(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId) {
        final LoanRequestResult loanRequestResult = askewsFacade.processLoan(contentProviderConsumer, contentProviderRecordId);

        if (loanRequestWasNotSuccessful(loanRequestResult)) {
            throwInternalServerErrorException(loanRequestResult.getErrorDesc().getValue(), loanRequestResult.getErrorCode());
        }

        return loanRequestResult.getLoanid().toString();
    }

    private boolean loanRequestWasNotSuccessful(LoanRequestResult loanRequestResult) {
        Integer requestStatus = loanRequestResult.getLoanRequestSuccess();
        return !LOAN_SUCCESS.equals(requestStatus);
    }

    private String getContentUrl(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId) {
        final LoanDetails loanDetails = tryToGetLoanLoanDetails(contentProviderConsumer, contentProviderLoanId);
        return getContentUrl(loanDetails);
    }

    private String getContentUrl(LoanDetails loanDetails) {
        final JAXBElement<String> downloadUrl = loanDetails.getDownloadURL();
        return downloadUrl.getValue();
    }

    private LoanDetails tryToGetLoanLoanDetails(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId) {
        LoanDetails loanDetails;
        int retryCount = 0;

        do {
            retryCount++;
            sleep(retryCount);
            loanDetails = getLoanDetails(contentProviderConsumer, contentProviderLoanId);
        } while (waitingToBeProcessed(loanDetails) && retryCount <= MAX_RETRIES);

        if (waitingToBeProcessed(loanDetails)) {
            throwInternalServerErrorException("Title has not yet been processed", loanDetails.getLoanStatus());
        }

        return loanDetails;
    }

    private void sleep(int retryCount) {
        if (retryCount > 1) {
            try {
                Thread.sleep(RETRY_WAIT_MILLIS);
            } catch (InterruptedException e) {
            }
            LOGGER.debug("Retrying getLoanDetails retryCount=" + retryCount);
        }
    }

    private LoanDetails getLoanDetails(ContentProviderConsumer contentProviderConsumer, String contentProviderLoanId) {
        final ArrayOfLoanDetails loanDetailsArray = askewsFacade.getLoanDetails(contentProviderConsumer, contentProviderLoanId);
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

    private boolean waitingToBeProcessed(LoanDetails loanDetail) {
        Integer loanStatus = loanDetail.getLoanStatus();
        return WAITING_TO_PROCESS.equals(loanStatus);
    }

    @Override
    public ContentLink getContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final String contentProviderLoanId = contentProviderLoanMetadata.getId();
        final LoanDetails loanDetail = getLoanDetails(contentProviderConsumer, contentProviderLoanId);

        if (titleHasNotBeenProcessed(loanDetail)) {
            throwInternalServerErrorException("Title has not yet been processed", loanDetail.getLoanStatus());
        }

        final String contentUrl = getContentUrl(loanDetail);
        final FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
        return createContent(contentUrl, formatDecoration);
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
