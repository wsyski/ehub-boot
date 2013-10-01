package com.axiell.ehub.provider.askews;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_AUTH_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_BARCODE;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_LOAN_DURATION;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_TOKEN_KEY;

import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.Formats;

@Component
public class AskewsDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AskewsDataAccessor.class);
    private static final String ASKEWS_FORMAT = "Askews";
    private static final Integer WAITING_TO_PROCESS = 1;
    private static final Integer TITLE_HAS_BEEN_PROCESSED = 4;
    private static final Integer LOAN_SUCCESS = 1;
    private static final int MAX_RETRIES = 60;
    private static final Integer RETRY_WAIT_MILLIS = 1000;
    private static final Integer ERROR_CODE_OK = 0;

    @Autowired(required = true)
    private IAskewsFacade askewsFacade;

    @Override
    public Formats getFormats(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, String language) {
	final Formats formats = new Formats();
	formats.addFormat(new Format(ASKEWS_FORMAT, ASKEWS_FORMAT, null, null));
	return formats;
    }

    @Override
    public ContentProviderLoan createLoan(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin, PendingLoan pendingLoan) {
	final Integer authId = getAuthId(contentProviderConsumer);
	final Integer duration = getDuration(contentProviderConsumer);
	final String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKEN_KEY);
	final Integer askewsLoanId = processLoan(contentProviderConsumer, pendingLoan, authId, duration, tokenKey);
	final String contentUrl = getContentUrl(contentProviderConsumer, authId, tokenKey, askewsLoanId);

	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final String formatId = pendingLoan.getContentProviderFormatId();
	final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);

	final IContent content = createContent(contentUrl, formatDecoration);

	final Date expirationDate = new DateTime().plusDays(duration).toDate();
	final String contentProviderLoanId = askewsLoanId.toString();
	final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata(contentProviderLoanId, contentProvider, expirationDate, formatDecoration);
	return new ContentProviderLoan(metadata, content);
    }

    private Integer getAuthId(ContentProviderConsumer contentProviderConsumer) {
	final String authId = contentProviderConsumer.getProperty(ASKEWS_AUTH_ID);
	return Integer.valueOf(authId);
    }
    
    private Integer getDuration(ContentProviderConsumer contentProviderConsumer) {
	final String duration = contentProviderConsumer.getProperty(ASKEWS_LOAN_DURATION);
	return Integer.valueOf(duration);
    }

    private Integer processLoan(final ContentProviderConsumer contentProviderConsumer, final PendingLoan pendingLoan, final Integer authId,
	    final Integer duration, final String tokenKey) {
	final Integer userId = getUserId(contentProviderConsumer);
	final LoanRequestResult loanRequestResult = askewsFacade.processLoan(userId, authId, pendingLoan.getContentProviderRecordId(), duration, tokenKey);

	if (loanRequestWasNotSucessful(loanRequestResult)) {
	    throwInternalServerErrorException(loanRequestResult.getErrorDesc().getValue(), loanRequestResult.getErrorCode());
	}

	return loanRequestResult.getLoanid();
    }

    private boolean loanRequestWasNotSucessful(LoanRequestResult loanRequestResult) {
	Integer requestStatus = loanRequestResult.getLoanRequestSuccess();
	return !LOAN_SUCCESS.equals(requestStatus);
    }

    private String getContentUrl(ContentProviderConsumer contentProviderConsumer, Integer authId, String tokenKey, Integer loanId) {
	final LoanDetails loanDetails = tryToGetLoanLoanDetails(contentProviderConsumer, authId, tokenKey, loanId);
	return getContentUrl(loanDetails);
    }

    private String getContentUrl(LoanDetails loanDetails) {
	final JAXBElement<String> downloadUrl = loanDetails.getDownloadURL();
	return downloadUrl.getValue();
    }

    private LoanDetails tryToGetLoanLoanDetails(ContentProviderConsumer contentProviderConsumer, Integer authId, String tokenKey, Integer loanId) {
	LoanDetails loanDetails = null;
	int retryCount = 0;

	do {
	    retryCount++;
	    sleep(retryCount);
	    loanDetails = getLoanDetails(contentProviderConsumer, authId, tokenKey, loanId);
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

    private LoanDetails getLoanDetails(ContentProviderConsumer contentProviderConsumer, Integer authId, String tokenKey, Integer loanId) {
	final Integer userId = getUserId(contentProviderConsumer);
	final ArrayOfLoanDetails loanDetailsArray = askewsFacade.getLoanDetails(userId, authId, loanId, tokenKey);
	final LoanDetails loanDetails = loanDetailsArray.getLoanDetails().get(0);
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
    public IContent getContent(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin,
	    ContentProviderLoanMetadata contentProviderLoanMetadata) {
	final Integer authId = getAuthId(contentProviderConsumer);
	final String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKEN_KEY);
	final Integer loanId = Integer.valueOf(contentProviderLoanMetadata.getId());

	final LoanDetails loanDetail = getLoanDetails(contentProviderConsumer, authId, tokenKey, loanId);

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

    private Integer getUserId(ContentProviderConsumer contentProviderConsumer) {
	final String barcode = contentProviderConsumer.getProperty(ASKEWS_BARCODE);
	final Integer authId = getAuthId(contentProviderConsumer);
	final String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKEN_KEY);
	final UserLookupResult userLookupResult = askewsFacade.getUserID(barcode, authId, tokenKey);

	if (userLookupWasNotSuccessful(userLookupResult)) {
	    throwInternalServerErrorException(userLookupResult.getErrorDesc().getValue(), userLookupResult.getErrorCode());
	}

	return userLookupResult.getUserid();
    }

    private boolean userLookupWasNotSuccessful(UserLookupResult userLookupResult) {
	final Integer errorCode = userLookupResult.getErrorCode();
	return !ERROR_CODE_OK.equals(errorCode);
    }

    private void throwInternalServerErrorException(String errorMessage, Integer errorCode) {
	ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ASKEWS);
	ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, String.valueOf(errorCode));
	throw new InternalServerErrorException(errorMessage, ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
    }
}
