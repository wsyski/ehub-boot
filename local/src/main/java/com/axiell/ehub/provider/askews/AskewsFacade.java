package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.IeBookService;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.logging.ISoapLoggingHandlerAppender;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import java.net.URL;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.*;

@Component
public class AskewsFacade implements IAskewsFacade {
    private static final String WSDL_LOCATION = "com/askews/api/askews.wsdl";
    private static final Integer ERROR_CODE_OK = 0;

    @Autowired
    private ISoapLoggingHandlerAppender soapLoggingHandlerAppender;


    private IeBookService askewsService;

    public AskewsFacade() {
        URL wsdlUrl = getClass().getClassLoader().getResource(WSDL_LOCATION);
        askewsService = new AskewsSoapService(wsdlUrl).getBasicHttpBindingIeBookService();
    }

    @PostConstruct
    public void init() {
        soapLoggingHandlerAppender.addLoggingHandler(askewsService);
    }

    @Override
    public LoanRequestResult processLoan(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final Patron patron) {
        final Integer userId = getUserId(patron,contentProviderConsumer);
        final Integer authId = getAuthId(contentProviderConsumer);
        final Integer duration = getDuration(contentProviderConsumer);
        final String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKEN_KEY);
        return askewsService.processLoan(userId, authId, contentProviderRecordId, duration, tokenKey);
    }

    private Integer getAuthId(ContentProviderConsumer contentProviderConsumer) {
        final String authId = contentProviderConsumer.getProperty(ASKEWS_AUTH_ID);
        return Integer.valueOf(authId);
    }

    private Integer getDuration(ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String duration = contentProvider.getProperty(ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS);
        return Integer.valueOf(duration);
    }

    @Override
    public ArrayOfLoanDetails getLoanDetails(final ContentProviderConsumer contentProviderConsumer, final String contentProviderLoanId, final Patron patron) {
        final Integer userId = getUserId(patron, contentProviderConsumer);
        final Integer authId = getAuthId(contentProviderConsumer);
        final Integer loanId = contentProviderLoanId == null ? null : Integer.valueOf(contentProviderLoanId);
        final String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKEN_KEY);
        return askewsService.getLoanDetails(userId, authId, null, loanId, tokenKey);
    }

    private Integer getUserId(final Patron patron, final ContentProviderConsumer contentProviderConsumer) {
        final String barcode = patron.getLibraryCard();
        final Integer authId = getAuthId(contentProviderConsumer);
        final String tokenKey = contentProviderConsumer.getProperty(ASKEWS_TOKEN_KEY);
        final UserLookupResult userLookupResult = askewsService.getUserID(barcode, authId, tokenKey);
        validateUserLookupWasSuccessful(userLookupResult);
        return userLookupResult.getUserid();
    }

    private void validateUserLookupWasSuccessful(final UserLookupResult userLookupResult) {
        if (userLookupWasNotSuccessful(userLookupResult)) {
            throwInternalServerErrorException(userLookupResult);
        }
    }

    private boolean userLookupWasNotSuccessful(UserLookupResult userLookupResult) {
        final Integer errorCode = userLookupResult.getErrorCode();
        return !ERROR_CODE_OK.equals(errorCode);
    }

    private void throwInternalServerErrorException(UserLookupResult userLookupResult) {
        final JAXBElement<String> errorDesc = userLookupResult.getErrorDesc();
        final String errorMessage = errorDesc.getValue();
        final Integer errorCode = userLookupResult.getErrorCode();
        ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProvider.CONTENT_PROVIDER_ASKEWS);
        ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, String.valueOf(errorCode));
        throw new InternalServerErrorException(errorMessage, ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
    }
}
