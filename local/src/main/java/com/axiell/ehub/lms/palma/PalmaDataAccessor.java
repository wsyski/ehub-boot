package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestErrorStatusType;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse;
import com.axiell.arena.services.palma.search.v267.service.SearchResponse;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.*;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.BaseLmsDataAccessor;
import com.axiell.ehub.lms.ILmsDataAccessor;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

/**
 * Palma implementation of the {@link ILmsDataAccessor}.
 */
@Component
@Qualifier("palmaDataAccessor")
public class PalmaDataAccessor extends BaseLmsDataAccessor implements ILmsDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PalmaDataAccessor.class);

    @Autowired
    private ILoansFacade loansFacade;
    @Autowired
    private ICatalogueFacade catalogueFacade;
    @Autowired
    private IResponseStatusChecker responseStatusChecker;

    @Override
    public CheckoutTestAnalysis checkoutTest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron,
                                             final boolean isLoanPerProduct, final Locale locale) {
        com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse checkOutTestResponse =
                loansFacade.checkOutTest(ehubConsumer, pendingLoan, patron, isLoanPerProduct);
        responseStatusChecker.checkResponseStatus(checkOutTestResponse.getStatus(), ehubConsumer, patron);
        return getCheckoutTestAnalysis(ehubConsumer, pendingLoan, patron, checkOutTestResponse);
    }

    @Override
    public LmsLoan checkout(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate, final Patron patron,
                            final boolean isLoanPerProduct, final Locale locale) {
        com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse checkOutResponse =
                loansFacade.checkOut(ehubConsumer, pendingLoan, expirationDate, patron, isLoanPerProduct);
        responseStatusChecker.checkResponseStatus(checkOutResponse.getStatus(), ehubConsumer, patron);
        return getLmsLoan(ehubConsumer, pendingLoan, patron, checkOutResponse);
    }

    @Override
    public String getMediaClass(final EhubConsumer ehubConsumer, final String contentProviderAlias, final String contentProviderRecordId, final Locale locale) {
        SearchResponse.SearchResult searchResult = catalogueFacade.search(ehubConsumer, contentProviderAlias, contentProviderRecordId);
        responseStatusChecker.checkResponseStatus(searchResult.getStatus(), ehubConsumer);
        String mediaClass = null;
        if (searchResult.getNofRecordsTotal() == 0) {
            LOGGER.error("Missing record contentProviderAlias: " + contentProviderAlias + " contentProviderRecordId: " + contentProviderRecordId);
        } else {
            mediaClass = searchResult.getCatalogueRecords().getCatalogueRecord().get(0).getMediaClass();
            if (searchResult.getNofRecordsTotal() > 1) {
                LOGGER.error("Duplicate records for contentProviderAlias: " + contentProviderAlias + " contentProviderRecordId: " + contentProviderRecordId);
            }
        }
        return mediaClass;
    }

    private LmsLoan getLmsLoan(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron,
                               final CheckOutResponse checkOutResponse) {
        String lmsLoanId;
        CheckOutResponse.CheckOutSuccess checkOutSuccess = checkOutResponse.getCheckOutSuccess();
        if (checkOutSuccess != null) {
            lmsLoanId = checkOutSuccess.getLoanId();
        } else {
            throw createCheckOutErrorException(checkOutResponse.getCheckOutError(), ehubConsumer, pendingLoan, patron);
        }
        return new LmsLoan(lmsLoanId);
    }

    private CheckoutTestAnalysis getCheckoutTestAnalysis(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron,
                                                         final CheckOutTestResponse checkOutTestResponse) {
        String lmsLoanId = null;
        Result result;
        CheckOutTestErrorStatusType testStatus = checkOutTestResponse.getTestStatus();
        if (testStatus != null) {
            switch (testStatus) {
                case NEW_LOAN:
                    result = Result.NEW_LOAN;
                    break;
                case ACTIVE_LOAN:
                    lmsLoanId = checkOutTestResponse.getLoanId();
                    result = Result.ACTIVE_LOAN;
                    break;
                case CHECK_OUT_DENIED:
                    throw createCheckOutTestCheckoutDeniedException(ehubConsumer, pendingLoan, patron);
                case INVALID_RECORD_ID:
                    throw createCheckOutTestNotFoundException(ehubConsumer, pendingLoan);
                default:
                    throw createCheckOutTestInternalErrorException(ehubConsumer, checkOutTestResponse.getTestStatus().value());
            }
            return new CheckoutTestAnalysis(result, lmsLoanId);
        } else {
            throw new InternalServerErrorException("CheckOutTestResponse testStatus can not be null");
        }
    }

    private RuntimeException createCheckOutErrorException(final CheckOutResponse.CheckOutError checkOutError, final EhubConsumer ehubConsumer,
                                                          final PendingLoan pendingLoan, final Patron patron) {
        Validate.isNotNull(checkOutError, ehubConsumer, "CheckOutError was null");
        Validate.isNotNull(checkOutError.getStatus(), ehubConsumer, "CheckOutError status was null");
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.lmsRecordId());
        switch (checkOutError.getStatus()) {
            case CHECK_OUT_DENIED:
                final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, patron.getLibraryCard());
                return new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED, argLmsRecordId, argLibraryCard, argEhubConsumerId);
            case INVALID_RECORD_ID:
                return new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
            default:
                final ErrorCauseArgument argStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, checkOutError.getStatus().value());
                return new InternalServerErrorException(LMS_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
        }
    }
}
