package com.axiell.ehub.provider.askews;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_AUTHID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_BARCODE;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_LOAN_DURATION;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ASKEWS_TOKENKEY;

import java.net.URL;
import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.IeBookService;
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
    private static final String WSDL_LOCATION = "com/askews/api/askews.wsdl";
    private static final Integer WAITING_TO_PROCESS = 1;
    private static final Integer TITLE_HAS_BEEN_PROCESSED = 4;
    private static final Integer LOAN_SUCCESS = 1;
    private static final Integer MAX_RETRIES = 60; // TODO: Make this configurable
    private static final Integer RETRY_WAIT_MILLIS = 1000; // TODO: Make this configurable

    private IeBookService askewsService;

    public AskewsDataAccessor() {
        URL wsdlUrl = getClass().getClassLoader().getResource(WSDL_LOCATION);
        askewsService = new AskewsSoapService(wsdlUrl).getBasicHttpBindingIeBookService();
    }

    @Override
    public Formats getFormats(ContentProviderConsumer contentProviderConsumer,
            String contentProviderRecordId,
            String language) {
        Formats formats = new Formats();
        formats.addFormat(new Format("Askews", "Askews", null, null));
        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(ContentProviderConsumer contentProviderConsumer,
            String libraryCard,
            String pin,
            PendingLoan pendingLoan) {
        Integer authId = Integer.parseInt(contentProviderConsumer.getProperty(ASKEWS_AUTHID));
        Integer duration = Integer.parseInt(contentProviderConsumer.getProperty(ASKEWS_LOAN_DURATION));
        String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKENKEY);
        LoanRequestResult loanRequestResult = askewsService.processLoan(getUserId(contentProviderConsumer), authId,
                pendingLoan.getContentProviderRecordId(), duration, tokenKey);

        if (loanRequestResult.getLoanRequestSuccess() != LOAN_SUCCESS) {
            throwException(loanRequestResult.getErrorDesc().getValue(), loanRequestResult.getErrorCode());
        }

        LoanDetails loanDetail = null;
        int retryCount = 0;

        do {
            retryCount++;

            if (retryCount > 1) {
                try {
                    Thread.sleep(RETRY_WAIT_MILLIS);
                } catch (InterruptedException e) {
                }
                
                LOGGER.debug("Retrying getLoanDetails retryCount=" + retryCount);
            }

            ArrayOfLoanDetails loanDetails = askewsService.getLoanDetails(getUserId(contentProviderConsumer), authId,
                    null, loanRequestResult.getLoanid(), tokenKey);
            loanDetail = loanDetails.getLoanDetails().get(0);

            if (loanDetail.isHasFailed()) {
                throwException(loanDetail.getErrorDesc().getValue(), loanDetail.getErrorCode());
            }
        } while (loanDetail.getLoanStatus() == WAITING_TO_PROCESS && retryCount <= MAX_RETRIES);

        if (loanDetail.getLoanStatus() == WAITING_TO_PROCESS) {
            throwException("Title has not yet been processed", loanDetail.getLoanStatus());
        }

        String contentUrl = loanDetail.getDownloadURL().getValue();
        ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        FormatDecoration formatDecoration = contentProvider.getFormatDecoration(pendingLoan
                .getContentProviderFormatId());
        IContent content = createContent(contentUrl, formatDecoration);
        Date expirationDate = new DateTime().plusDays(duration).toDate();
        ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata(loanRequestResult.getLoanid()
                .toString(), contentProvider, expirationDate, formatDecoration);
        return new ContentProviderLoan(metadata, content);
    }

    @Override
    public IContent getContent(ContentProviderConsumer contentProviderConsumer,
            String libraryCard,
            String pin,
            ContentProviderLoanMetadata contentProviderLoanMetadata) {
        Integer authId = Integer.parseInt(contentProviderConsumer.getProperty(ASKEWS_AUTHID));
        String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKENKEY);
        Integer loanId = Integer.parseInt(contentProviderLoanMetadata.getId());

        ArrayOfLoanDetails loanDetails = askewsService.getLoanDetails(getUserId(contentProviderConsumer), authId,
                null, loanId, tokenKey);
        LoanDetails loanDetail = loanDetails.getLoanDetails().get(0);

        if (loanDetail.isHasFailed()) {
            throwException(loanDetail.getErrorDesc().getValue(), loanDetail.getErrorCode());
        }

        if (loanDetail.getLoanStatus() != TITLE_HAS_BEEN_PROCESSED) {
            throwException("Title has not yet been processed", loanDetail.getLoanStatus());
        }

        return createContent(loanDetail.getDownloadURL().getValue(), contentProviderLoanMetadata.getFormatDecoration());
    }

    private Integer getUserId(ContentProviderConsumer contentProviderConsumer) {
        String barcode = contentProviderConsumer.getProperty(ASKEWS_BARCODE);
        Integer authId = Integer.parseInt(contentProviderConsumer.getProperty(ASKEWS_AUTHID));
        String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKENKEY);
        UserLookupResult userLookupResult = askewsService.getUserID(barcode, authId, tokenKey);

        if (userLookupResult.getErrorCode() != 0) {
            throwException(userLookupResult.getErrorDesc().getValue(), userLookupResult.getErrorCode());
        }

        return userLookupResult.getUserid();
    }

    private void throwException(String errorMessage, Integer errorCode) {
        ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME,
                ContentProviderName.ASKEWS);
        ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS,
                String.valueOf(errorCode));

        throw new InternalServerErrorException(errorMessage, ErrorCause.CONTENT_PROVIDER_ERROR,
                argContentProviderName, argContentProviderStatus);
    }
}
