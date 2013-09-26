/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.palma;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.axiell.ehub.util.LoggingHandler;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import com.axiell.arena.services.palma.loans.CheckOut;
import com.axiell.arena.services.palma.loans.CheckOutTest;
import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.arena.services.palma.loans.LoansPalmaService;
import com.axiell.arena.services.palma.patron.checkoutrequest.CheckOutRequest;
import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse;
import com.axiell.arena.services.palma.patron.checkouttestrequest.CheckOutTestRequest;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse;
import com.axiell.arena.services.palma.util.status.Status;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.util.XjcSupport;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

/**
 * Default implementation of the {@link IPalmaDataAccessor}.
 */
@Component
final class PalmaDataAccessor implements IPalmaDataAccessor {
    private static final String STATUS_OK = "ok";
    private static final String STATUS_ERROR = "error";
    private static final String MESSAGE_BLOCKED_BORR_CARD = "blockedBorrCard";
    private static final String MESSAGE_BORR_CARD_NOT_FOUND = "borrCardNotFound";
    private static final String MESSAGE_INVALID_ACCOUNT_CARD = "invalidAccountCard";
    private static final String MESSAGE_INVALID_BORR_CARD = "invalidBorrCard";
    private static final String MESSAGE_INVALID_PATRON = "invalidPatron";
    private static final String MESSAGE_INVALID_PIN_CODE = "invalidPinCode";
    private static final String INTERNAL_ERROR_MESSAGE = "Internal error in palma";
    private Map<URL, Loans> loanPorts = new HashMap<>();

    /**
     * @see com.axiell.ehub.lms.palma.IPalmaDataAccessor#checkoutTest(com.axiell.ehub.consumer.EhubConsumer, com.axiell.ehub.loan.PendingLoan, String, String)
     */
    @Override
    public CheckoutTestAnalysis checkoutTest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final String libraryCard, final String pin) {
        String agencyMemberIdentifier = getAgencyMemberIdentifier(ehubConsumer);
        com.axiell.arena.services.palma.patron.checkouttestrequest.ObjectFactory requestObjectFactory =
                new com.axiell.arena.services.palma.patron.checkouttestrequest.ObjectFactory();
        CheckOutTestRequest checkOutTestRequest = requestObjectFactory.createCheckOutTestRequest();
        checkOutTestRequest.setArenaMember(agencyMemberIdentifier);
        checkOutTestRequest.setRecordId(pendingLoan.getLmsRecordId());
        checkOutTestRequest.setContentProviderFormatId(pendingLoan.getContentProviderFormatId());
        checkOutTestRequest.setContentProviderName(pendingLoan.getContentProviderName());
        checkOutTestRequest.setUser(libraryCard);
        checkOutTestRequest.setPassword(pin);
        com.axiell.arena.services.palma.loans.ObjectFactory objectFactory = new com.axiell.arena.services.palma.loans.ObjectFactory();
        CheckOutTest checkOutTest = objectFactory.createCheckOutTest();
        checkOutTest.setCheckOutTestRequest(checkOutTestRequest);
        Loans loansService = getLoansService(ehubConsumer);
        com.axiell.arena.services.palma.loans.CheckOutTestResponse loansCheckOutTestResponse = loansService.checkOutTest(checkOutTest);
        CheckOutTestResponse checkOutTestResponse = loansCheckOutTestResponse.getCheckOutTestResponse();
        String lmsLoanId = null;
        Result result;
        checkResponseStatus(checkOutTestResponse.getStatus(), ehubConsumer, libraryCard);
        Validate.notNull(checkOutTestResponse.getTestStatus(), "Test status can not be null");
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
        switch (checkOutTestResponse.getTestStatus()) {
            case NEW_LOAN:
                result = Result.NEW_LOAN;
                break;
            case ACTIVE_LOAN:
                lmsLoanId = checkOutTestResponse.getLoanId();
                result = Result.ACTIVE_LOAN;
                break;
            case CHECK_OUT_DENIED:
                final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, libraryCard);
                throw new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED, argLmsRecordId, argLibraryCard, argEhubConsumerId);
            case INVALID_RECORD_ID:
                throw new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
            default:
                final ErrorCauseArgument argStatus =
                        new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, checkOutTestResponse.getTestStatus().value());
                throw new InternalServerErrorException(INTERNAL_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
        }
        return new CheckoutTestAnalysis(result, lmsLoanId);
    }

    /**
     * @see com.axiell.ehub.lms.palma.IPalmaDataAccessor#checkout(com.axiell.ehub.consumer.EhubConsumer, com.axiell.ehub.loan.PendingLoan, java.util.Date, String, String)
     */
    @Override
    public LmsLoan checkout(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate, final String libraryCard, final String pin)
            throws ForbiddenException {
        String agencyMemberIdentifier = getAgencyMemberIdentifier(ehubConsumer);
        com.axiell.arena.services.palma.patron.checkoutrequest.ObjectFactory requestObjectFactory =
                new com.axiell.arena.services.palma.patron.checkoutrequest.ObjectFactory();
        CheckOutRequest checkOutRequest = requestObjectFactory.createCheckOutRequest();
        checkOutRequest.setArenaMember(agencyMemberIdentifier);
        checkOutRequest.setRecordId(pendingLoan.getLmsRecordId());
        checkOutRequest.setExpirationDate(XjcSupport.date2XMLGregorianCalendar(expirationDate));
        checkOutRequest.setContentProviderFormatId(pendingLoan.getContentProviderFormatId());
        checkOutRequest.setContentProviderName(pendingLoan.getContentProviderName());
        checkOutRequest.setUser(libraryCard);
        checkOutRequest.setPassword(pin);
        com.axiell.arena.services.palma.loans.ObjectFactory objectFactory = new com.axiell.arena.services.palma.loans.ObjectFactory();
        CheckOut checkOut = objectFactory.createCheckOut();
        checkOut.setCheckOutRequest(checkOutRequest);
        Loans loansService = getLoansService(ehubConsumer);
        com.axiell.arena.services.palma.loans.CheckOutResponse loansCheckOutResponse = loansService.checkOut(checkOut);
        CheckOutResponse checkOutResponse = loansCheckOutResponse.getCheckOutResponse();
        String lmsLoanId;
        checkResponseStatus(checkOutResponse.getStatus(), ehubConsumer, libraryCard);
        CheckOutResponse.CheckOutError checkOutError = checkOutResponse.getCheckOutError();
        CheckOutResponse.CheckOutSuccess checkOutSuccess = checkOutResponse.getCheckOutSuccess();
        if (checkOutSuccess != null) {
            lmsLoanId = checkOutSuccess.getLoanId();
        } else {
            Validate.notNull(checkOutError, "Checkout error expected");
            Validate.notNull(checkOutError.getStatus(), "Checkout error status expected");
            final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
            final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.getLmsRecordId());
            switch (checkOutError.getStatus()) {
                case CHECK_OUT_DENIED:
                    final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, libraryCard);
                    throw new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED, argLmsRecordId, argLibraryCard, argEhubConsumerId);
                case INVALID_RECORD_ID:
                    throw new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
                default:
                    final ErrorCauseArgument argStatus =
                            new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, checkOutError.getStatus().value());
                    throw new InternalServerErrorException(INTERNAL_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
            }
        }
        return new LmsLoan(lmsLoanId);
    }

    private void checkResponseStatus(final Status status, final EhubConsumer ehubConsumer, final String libraryCard)
            throws ForbiddenException {
        String responseStatus = status.getType() == null ? STATUS_ERROR : status.getType();
        if (!responseStatus.equals(STATUS_OK)) {
            final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
            final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, libraryCard);
            if (status.getMessage() != null) {
                responseStatus = status.getMessage();
                if (responseStatus.equals(MESSAGE_INVALID_PIN_CODE)) {
                    throw new ForbiddenException(ErrorCause.LMS_INVALID_PIN_CODE, argLibraryCard, argEhubConsumerId);
                } else if (responseStatus.equals(MESSAGE_BLOCKED_BORR_CARD)) {
                    throw new ForbiddenException(ErrorCause.LMS_BLOCKED_LIBRARY_CARD, argLibraryCard, argEhubConsumerId);
                } else if (responseStatus.equals(MESSAGE_BORR_CARD_NOT_FOUND)) {
                    throw new ForbiddenException(ErrorCause.LMS_LIBRARY_CARD_NOT_FOUND, argLibraryCard, argEhubConsumerId);
                } else if (responseStatus.equals(MESSAGE_INVALID_ACCOUNT_CARD) || responseStatus.equals(MESSAGE_INVALID_BORR_CARD) ||
                        responseStatus.equals(MESSAGE_INVALID_PATRON)) {
                    throw new ForbiddenException(ErrorCause.LMS_INVALID_LIBRARY_CARD, argLibraryCard, argEhubConsumerId);
                }
            }
            final ErrorCauseArgument argStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, responseStatus);
            throw new InternalServerErrorException(INTERNAL_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
        }
    }

    private Loans getLoansService(final EhubConsumer ehubConsumer) {
        String arenaPalmaUrl = getPalmaUrl(ehubConsumer);
        URL loansServiceWsdlURL;
        try {
            loansServiceWsdlURL = new URL(arenaPalmaUrl + "/loans?wsdl");
        } catch (MalformedURLException ex) {
            final ErrorCauseArgument argument0 =
                    new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_PROPERTY_KEY, EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL.name());
            final ErrorCauseArgument argument1 =
                    new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_PROPERTY_VALUE, arenaPalmaUrl);
            final ErrorCauseArgument argument2 = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
            throw new InternalServerErrorException(INTERNAL_ERROR_MESSAGE, ErrorCause.EHUB_CONSUMER_INVALID_PROPERTY, argument0, argument1, argument2);
        }
        Loans loanPort = loanPorts.get(loansServiceWsdlURL);
        if (loanPort == null) {
            LoansPalmaService loansPalmaService = new LoansPalmaService(loansServiceWsdlURL);
            loanPort = loansPalmaService.getLoans();
            BindingProvider bp = (BindingProvider) loanPort;
            Binding binding = bp.getBinding();

            List<Handler> handlerList = binding.getHandlerChain();
            if (handlerList == null)
                handlerList = new ArrayList<>();
            LoggingHandler loggingHandler = new LoggingHandler();
            handlerList.add(loggingHandler);
            binding.setHandlerChain(handlerList);

            /* CXF
            Client client = ClientProxy.getClient(loansService);
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());
            */
            loanPorts.put(loansServiceWsdlURL, loanPort);
        }
        return loanPort;
    }

    private String getPalmaUrl(final EhubConsumer ehubConsumer) {
        String palmaUrl = ehubConsumer.getProperties().get(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL);
        Validate.notBlank(palmaUrl, EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL + " can not be blank");
        return palmaUrl;
    }

    private String getAgencyMemberIdentifier(final EhubConsumer ehubConsumer) {
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER);
        Validate.notBlank(agencyMemberIdentifier, EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER + " can not be blank");
        return agencyMemberIdentifier;
    }
}
