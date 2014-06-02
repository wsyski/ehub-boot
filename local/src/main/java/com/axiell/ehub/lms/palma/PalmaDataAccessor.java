/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.palma;

import com.axiell.ehub.services.palma.loans.CheckOut;
import com.axiell.ehub.services.palma.loans.CheckOutTest;
import com.axiell.ehub.services.palma.patron.checkoutrequest.CheckOutRequest;
import com.axiell.ehub.services.palma.patron.checkoutresponse.CheckOutResponse;
import com.axiell.ehub.services.palma.patron.checkouttestrequest.CheckOutTestRequest;
import com.axiell.ehub.services.palma.patron.checkouttestresponse.CheckOutTestResponse;
import com.axiell.ehub.services.palma.util.status.Status;
import com.axiell.ehub.*;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.util.Validate;
import com.axiell.ehub.util.XjcSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Default implementation of the {@link IPalmaDataAccessor}.
 */
@Component
final class PalmaDataAccessor implements IPalmaDataAccessor {
    private static final String STATUS_OK = "ok";
    private static final String MESSAGE_BLOCKED_BORR_CARD = "blockedBorrCard";
    private static final String MESSAGE_BORR_CARD_NOT_FOUND = "borrCardNotFound";
    private static final String MESSAGE_INVALID_ACCOUNT_CARD = "invalidAccountCard";
    private static final String MESSAGE_INVALID_BORR_CARD = "invalidBorrCard";
    private static final String MESSAGE_INVALID_PATRON = "invalidPatron";
    private static final String MESSAGE_INVALID_PIN_CODE = "invalidPinCode";
    private static final String LMS_ERROR_MESSAGE = "Error in lms";

    @Autowired(required = true)
    private IPalmaFacadeFactory palmaFacadeFactory;

    /**
     * @see com.axiell.ehub.lms.palma.IPalmaDataAccessor#checkoutTest(com.axiell.ehub.consumer.EhubConsumer, com.axiell.ehub.loan.PendingLoan, String, String)
     */
    @Override
    public CheckoutTestAnalysis checkoutTest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final String libraryCard, final String pin) {
        String agencyMemberIdentifier = getAgencyMemberIdentifier(ehubConsumer);
        CheckOutTest checkOutTest = createCheckOutTest(agencyMemberIdentifier, pendingLoan, libraryCard, pin);
        IPalmaFacade palmaFacade = getPalmaFacade(ehubConsumer);
        com.axiell.ehub.services.palma.loans.CheckOutTestResponse loansCheckOutTestResponse = palmaFacade.checkOutTest(checkOutTest);
        CheckOutTestResponse checkOutTestResponse = loansCheckOutTestResponse.getCheckOutTestResponse();
        checkCheckOutTestResponseStatus(checkOutTestResponse, ehubConsumer, libraryCard);
        return getCheckoutTestAnalysis(ehubConsumer, pendingLoan, libraryCard, checkOutTestResponse);
    }

    /**
     * @see com.axiell.ehub.lms.palma.IPalmaDataAccessor#checkout(com.axiell.ehub.consumer.EhubConsumer, com.axiell.ehub.loan.PendingLoan, java.util.Date, String, String)
     */
    @Override
    public LmsLoan checkout(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate, final String libraryCard,
                            final String pin) {
        String agencyMemberIdentifier = getAgencyMemberIdentifier(ehubConsumer);
        CheckOut checkOut = createCheckOut(agencyMemberIdentifier, pendingLoan, libraryCard, pin, expirationDate);
        IPalmaFacade palmaFacade = getPalmaFacade(ehubConsumer);
        com.axiell.ehub.services.palma.loans.CheckOutResponse loansCheckOutResponse = palmaFacade.checkOut(checkOut);
        CheckOutResponse checkOutResponse = loansCheckOutResponse.getCheckOutResponse();
        checkCheckOutResponseStatus(checkOutResponse, ehubConsumer, libraryCard);
        return getLmsLoan(ehubConsumer, pendingLoan, libraryCard, checkOutResponse);
    }

    private LmsLoan getLmsLoan(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final String libraryCard,
                               final CheckOutResponse checkOutResponse) {
        String lmsLoanId;
        CheckOutResponse.CheckOutSuccess checkOutSuccess = checkOutResponse.getCheckOutSuccess();
        if (checkOutSuccess != null) {
            lmsLoanId = checkOutSuccess.getLoanId();
        } else {
            throw createCheckOutErrorException(checkOutResponse.getCheckOutError(), ehubConsumer, pendingLoan, libraryCard);
        }
        return new LmsLoan(lmsLoanId);
    }

    private CheckoutTestAnalysis getCheckoutTestAnalysis(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final String libraryCard,
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
                throw createCheckOutTestCheckoutDeniedException(ehubConsumer, pendingLoan, libraryCard);
            case INVALID_RECORD_ID:
                throw createCheckOutTestNotFoundException(ehubConsumer, pendingLoan);
            default:
                throw createCheckOutTestInternalErrorException(ehubConsumer, checkOutTestResponse);
        }
        return new CheckoutTestAnalysis(result, lmsLoanId);
    }

    private static ForbiddenException createCheckOutTestCheckoutDeniedException(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan,
                                                                                final String libraryCard) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
        final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, libraryCard);
        return new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED, argLmsRecordId, argLibraryCard, argEhubConsumerId);
    }

    private static NotFoundException createCheckOutTestNotFoundException(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
        return new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
    }

    private static InternalServerErrorException createCheckOutTestInternalErrorException(final EhubConsumer ehubConsumer,
                                                                                         final CheckOutTestResponse checkOutTestResponse) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argStatus =
                new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, checkOutTestResponse.getTestStatus().value());
        return new InternalServerErrorException(LMS_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
    }

    private static CheckOutTest createCheckOutTest(final String agencyMemberIdentifier, final PendingLoan pendingLoan, final String libraryCard,
                                                   final String pin) {
        com.axiell.ehub.services.palma.patron.checkouttestrequest.ObjectFactory checkouttestrequestObjectFactory =
                new com.axiell.ehub.services.palma.patron.checkouttestrequest.ObjectFactory();
        CheckOutTestRequest checkOutTestRequest = checkouttestrequestObjectFactory.createCheckOutTestRequest();
        checkOutTestRequest.setArenaMember(agencyMemberIdentifier);
        checkOutTestRequest.setRecordId(pendingLoan.getLmsRecordId());
        checkOutTestRequest.setContentProviderFormatId(pendingLoan.getContentProviderFormatId());
        checkOutTestRequest.setContentProviderName(pendingLoan.getContentProviderName());
        checkOutTestRequest.setUser(libraryCard);
        checkOutTestRequest.setPassword(pin);
        com.axiell.ehub.services.palma.loans.ObjectFactory loansObjectFactory = new com.axiell.ehub.services.palma.loans.ObjectFactory();
        CheckOutTest checkOutTest = loansObjectFactory.createCheckOutTest();
        checkOutTest.setCheckOutTestRequest(checkOutTestRequest);
        return checkOutTest;
    }

    private static RuntimeException createCheckOutErrorException(final CheckOutResponse.CheckOutError checkOutError, final EhubConsumer ehubConsumer,
                                                                 final PendingLoan pendingLoan, final String libraryCard) {
        Validate.isNotNull(checkOutError, ehubConsumer, "CheckOutError was null");
        Validate.isNotNull(checkOutError.getStatus(), ehubConsumer, "CheckOutError status was null");
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
        switch (checkOutError.getStatus()) {
            case CHECK_OUT_DENIED:
                final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, libraryCard);
                return new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED, argLmsRecordId, argLibraryCard, argEhubConsumerId);
            case INVALID_RECORD_ID:
                return new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
            default:
                final ErrorCauseArgument argStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, checkOutError.getStatus().value());
                return new InternalServerErrorException(LMS_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
        }
    }

    private static CheckOut createCheckOut(final String agencyMemberIdentifier, final PendingLoan pendingLoan, final String libraryCard,
                                           final String pin, final Date expirationDate) {
        com.axiell.ehub.services.palma.patron.checkoutrequest.ObjectFactory checkoutrequestObjectFactory =
                new com.axiell.ehub.services.palma.patron.checkoutrequest.ObjectFactory();
        CheckOutRequest checkOutRequest = checkoutrequestObjectFactory.createCheckOutRequest();
        checkOutRequest.setArenaMember(agencyMemberIdentifier);
        checkOutRequest.setRecordId(pendingLoan.getLmsRecordId());
        checkOutRequest.setExpirationDate(XjcSupport.toXmlGregorianCalendar(expirationDate));
        checkOutRequest.setContentProviderFormatId(pendingLoan.getContentProviderFormatId());
        checkOutRequest.setContentProviderName(pendingLoan.getContentProviderName());
        checkOutRequest.setUser(libraryCard);
        checkOutRequest.setPassword(pin);
        com.axiell.ehub.services.palma.loans.ObjectFactory loansObjectFactory = new com.axiell.ehub.services.palma.loans.ObjectFactory();
        CheckOut checkOut = loansObjectFactory.createCheckOut();
        checkOut.setCheckOutRequest(checkOutRequest);
        return checkOut;
    }

    private static void checkCheckOutTestResponseStatus(final CheckOutTestResponse checkOutTestResponse, final EhubConsumer ehubConsumer,
                                                        final String libraryCard) {
        Validate.isNotNull(checkOutTestResponse, ehubConsumer, "CheckOutTestResponse was null");
        Validate.isNotNull(checkOutTestResponse.getTestStatus(), "CheckOutTestResponse testStatus was null");
        checkResponseStatus(checkOutTestResponse.getStatus(), ehubConsumer, libraryCard);
    }

    private static void checkCheckOutResponseStatus(final CheckOutResponse checkOutResponse, final EhubConsumer ehubConsumer, final String libraryCard) {
        Validate.isNotNull(checkOutResponse, ehubConsumer, "CheckOutResponse status was null");
        Validate.isNotNull(checkOutResponse.getCheckOutSuccess() == null ? checkOutResponse.getCheckOutError() : checkOutResponse.getCheckOutSuccess(),
                ehubConsumer, "CheckOutResponse checkOutSuccess and checkOutError were null");
        checkResponseStatus(checkOutResponse.getStatus(), ehubConsumer, libraryCard);
    }

    private static void checkResponseStatus(final Status status, final EhubConsumer ehubConsumer, final String libraryCard) {
        Validate.isNotNull(status, ehubConsumer, "Error status was null");
        Validate.isNotNull(status.getType(), ehubConsumer, "Status type was null");
        String statusType = status.getType();
        if (!statusType.equals(STATUS_OK)) {
            final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
            final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, libraryCard);
            String statusMessage = status.getMessage();
            if (statusMessage != null) {
                switch (statusMessage) {
                    case MESSAGE_INVALID_PIN_CODE:
                        throw new ForbiddenException(ErrorCause.LMS_INVALID_PIN_CODE, argLibraryCard, argEhubConsumerId);
                    case MESSAGE_BLOCKED_BORR_CARD:
                        throw new ForbiddenException(ErrorCause.LMS_BLOCKED_LIBRARY_CARD, argLibraryCard, argEhubConsumerId);
                    case MESSAGE_BORR_CARD_NOT_FOUND:
                        throw new ForbiddenException(ErrorCause.LMS_LIBRARY_CARD_NOT_FOUND, argLibraryCard, argEhubConsumerId);
                    case MESSAGE_INVALID_ACCOUNT_CARD:
                    case MESSAGE_INVALID_BORR_CARD:
                    case MESSAGE_INVALID_PATRON:
                        throw new ForbiddenException(ErrorCause.LMS_INVALID_LIBRARY_CARD, argLibraryCard, argEhubConsumerId);
                }
            }
            final ErrorCauseArgument argStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, statusMessage);
            throw new InternalServerErrorException(LMS_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
        }
    }

    private static String getAgencyMemberIdentifier(final EhubConsumer ehubConsumer) {
        EhubConsumer.EhubConsumerPropertyKey arenaAgencyMemberIdentifierKey = EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER;
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(arenaAgencyMemberIdentifierKey);
        Validate.isNotBlank(agencyMemberIdentifier, ehubConsumer, agencyMemberIdentifier + " is blank");
        return agencyMemberIdentifier;
    }

    private IPalmaFacade getPalmaFacade(final EhubConsumer ehubConsumer) {
        return palmaFacadeFactory.getInstance(ehubConsumer);
    }

    void setPalmaFacadeFactory(final IPalmaFacadeFactory palmaFacadeFactory) {
        this.palmaFacadeFactory = palmaFacadeFactory;
    }
}
