/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse;
import com.axiell.ehub.*;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.axiell.arena.services.palma.v267.patron.AuthenticatePatronResponse.AuthenticatePatronResult;

/**
 * Default implementation of the {@link IPalmaDataAccessor}.
 */
@Component
class PalmaDataAccessor implements IPalmaDataAccessor {
    private static final String LMS_ERROR_MESSAGE = "Error in lms";

    @Autowired
    private ILoansFacade loansFacade;
    @Autowired
    private IPatronFacade patronFacade;
    @Autowired
    private IResponseStatusChecker responseStatusChecker;

    @Override
    public Patron authenticatePatron(final EhubConsumer ehubConsumer, final String patronId, final String libraryCard, final String pin) {
        Patron.Builder patronBuilder = new Patron.Builder(libraryCard, pin).id(patronId);

        if (patronBuilder.hasCardButNoId())
            propagatePatronId(ehubConsumer, libraryCard, pin, patronBuilder);

        return patronBuilder.build();
    }

    private void propagatePatronId(EhubConsumer ehubConsumer, String libraryCard, String pin, Patron.Builder patronBuilder) {
        final AuthenticatePatronResult authenticatePatronResult = patronFacade.authenticatePatron(ehubConsumer, libraryCard, pin);
        responseStatusChecker.check267ResponseStatus(authenticatePatronResult.getStatus(), ehubConsumer, patronBuilder);
        final String patronId = authenticatePatronResult.getPatronId();
        patronBuilder.id(patronId);
    }

    @Override
    public CheckoutTestAnalysis checkoutTest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron) {
        com.axiell.arena.services.palma.loans.CheckOutTestResponse loansCheckOutTestResponse = loansFacade.checkOutTest(ehubConsumer, pendingLoan, patron);
        CheckOutTestResponse checkOutTestResponse = loansCheckOutTestResponse.getCheckOutTestResponse();
        responseStatusChecker.checkResponseStatus(checkOutTestResponse.getStatus(), ehubConsumer, patron);
        return getCheckoutTestAnalysis(ehubConsumer, pendingLoan, patron, checkOutTestResponse);
    }

    @Override
    public LmsLoan checkout(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate, final Patron patron) {
        com.axiell.arena.services.palma.loans.CheckOutResponse loansCheckOutResponse = loansFacade.checkOut(ehubConsumer, pendingLoan, expirationDate, patron);
        CheckOutResponse checkOutResponse = loansCheckOutResponse.getCheckOutResponse();
        responseStatusChecker.checkResponseStatus(checkOutResponse.getStatus(), ehubConsumer, patron);
        return getLmsLoan(ehubConsumer, pendingLoan, patron, checkOutResponse);
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
        switch (checkOutTestResponse.getTestStatus()) {
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
                throw createCheckOutTestInternalErrorException(ehubConsumer, checkOutTestResponse);
        }
        return new CheckoutTestAnalysis(result, lmsLoanId);
    }

    private ForbiddenException createCheckOutTestCheckoutDeniedException(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan,
                                                                         final Patron patron) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
        final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, patron.getLibraryCard());
        return new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED, argLmsRecordId, argLibraryCard, argEhubConsumerId);
    }

    private NotFoundException createCheckOutTestNotFoundException(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
        return new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
    }

    private InternalServerErrorException createCheckOutTestInternalErrorException(final EhubConsumer ehubConsumer,
                                                                                  final CheckOutTestResponse checkOutTestResponse) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argStatus =
                new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, checkOutTestResponse.getTestStatus().value());
        return new InternalServerErrorException(LMS_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
    }

    private RuntimeException createCheckOutErrorException(final CheckOutResponse.CheckOutError checkOutError, final EhubConsumer ehubConsumer,
                                                          final PendingLoan pendingLoan, final Patron patron) {
        Validate.isNotNull(checkOutError, ehubConsumer, "CheckOutError was null");
        Validate.isNotNull(checkOutError.getStatus(), ehubConsumer, "CheckOutError status was null");
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
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
