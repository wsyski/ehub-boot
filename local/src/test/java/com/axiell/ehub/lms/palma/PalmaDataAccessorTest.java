package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.CheckOut;
import com.axiell.arena.services.palma.loans.CheckOutResponse;
import com.axiell.arena.services.palma.loans.CheckOutTest;
import com.axiell.arena.services.palma.loans.CheckOutTestResponse;
import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutErrorStatusType;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestErrorStatusType;
import com.axiell.arena.services.palma.util.ISOCurrencyCodeType;
import com.axiell.arena.services.palma.util.v267.status.Status;
import com.axiell.arena.services.palma.v267.patron.AuthenticatePatronResponse;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.axiell.arena.services.palma.v267.patron.AuthenticatePatronResponse.AuthenticatePatronResult;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PalmaDataAccessorTest {
    private static final String STATUS_OK = "ok";
    private static final String LMS_RECORD_ID = "recordId";
    private static final String PROVIDER_FORMAT_ID = "providerFormatId";
    private static final String PROVIDER_RECORD_ID = "providerRecordId";
    private static final String LIBRARY_CARD = "libraryCard";
    private static final String PIN = "pin";
    private static final String LOAN_ID = "loanId";
    private static final String PALMA_URL = "http://localhost:16521/ehub.pa.palma";
    private static final String AGENCY_M_IDENTIFIER = "agencyMemberIdentifier";
    private static final long EHUB_CONSUMER_ID = 1L;
    private static final String PATRON_ID = "1";

    private PalmaDataAccessor underTest;
    @Mock
    private ILoansFacade loansFacade;
    @Mock
    private IPatronFacade patronFacade;
    @Mock
    private EhubConsumer ehubConsumer;
    private PendingLoan pendingLoan;
    private CheckoutTestAnalysis preCheckoutAnalysis;
    private LmsLoan lmsLoan;

    private String patronId;
    private String libraryCard;
    private String pin;
    @Mock
    private AuthenticatePatronResult authenticatePatronResult;
    @Mock
    private Status authenticatePatronResultStatus;
    @Mock
    private Patron patron;
    private Patron actualPatron;

    @Test
    public void authenticatePatron_noCardPin() {
        whenAuthenticatePatron();
        thenActualPatronIsNotNull();
        thenActualHasNoId();
        thenActualPatronHasNoCard();
        thenActualPatronHasNoPin();
    }

    private void whenAuthenticatePatron() {
        actualPatron = underTest.authenticatePatron(ehubConsumer, patronId, libraryCard, pin);
    }

    private void thenActualPatronIsNotNull() {
        assertNotNull(actualPatron);
    }

    private void thenActualHasNoId() {
        assertFalse(actualPatron.hasId());
    }

    private void thenActualPatronHasNoCard() {
        assertFalse(actualPatron.hasLibraryCard());
    }

    private void thenActualPatronHasNoPin() {
        assertNull(actualPatron.getPin());
    }

    @Test
    public void authenticatePatron_noPatronIdButCardPin() {
        givenAuthenticatePatronResultStatusTypeOk();
        givenAuthenticatePatronResultStatus();
        givenPatronIdInAuthenticatePatronResult();
        givenAuthenticatePatronResult();
        givenCard();
        givenPin();
        whenAuthenticatePatron();
        thenActualPatronIsNotNull();
        thenActualPatronHasExpectedId();
        thenActualPatronHasExpectedCard();
        thenActualPatronHasExpectedPin();
        thenAuthenticatePatronInPatronFacadeIsInvoked();
    }

    private void thenAuthenticatePatronInPatronFacadeIsInvoked() {
        verify(patronFacade, times(1)).authenticatePatron(any(EhubConsumer.class), anyString(), anyString());
    }

    @Test
    public void authenticatePatron_patronIdCardPin() {
        givenAuthenticatePatronResultStatusTypeOk();
        givenAuthenticatePatronResultStatus();
        givenPatronIdInAuthenticatePatronResult();
        givenAuthenticatePatronResult();
        givenPatronId();
        givenCard();
        givenPin();
        whenAuthenticatePatron();
        thenActualPatronIsNotNull();
        thenActualPatronHasExpectedId();
        thenActualPatronHasExpectedCard();
        thenActualPatronHasExpectedPin();
        thenAuthenticatePatronInPatronFacadeIsNeverInvoked();
    }

    private void thenAuthenticatePatronInPatronFacadeIsNeverInvoked() {
        verify(patronFacade, never()).authenticatePatron(any(EhubConsumer.class), anyString(), anyString());
    }

    private void givenPatronId() {
        patronId = PATRON_ID;
    }

    private void givenAuthenticatePatronResultStatusTypeOk() {
        given(authenticatePatronResultStatus.getType()).willReturn(STATUS_OK);
    }

    private void givenAuthenticatePatronResultStatus() {
        given(authenticatePatronResult.getStatus()).willReturn(authenticatePatronResultStatus);
    }

    private void givenPatronIdInAuthenticatePatronResult() {
        given(authenticatePatronResult.getPatronId()).willReturn(PATRON_ID);
    }

    private void givenAuthenticatePatronResult() {
        given(patronFacade.authenticatePatron(any(EhubConsumer.class), anyString(), anyString())).willReturn(authenticatePatronResult);
    }

    private void givenCard() {
        libraryCard = LIBRARY_CARD;
    }

    private void givenPin() {
        pin = PIN;
    }

    private void thenActualPatronHasExpectedId() {
        assertEquals(PATRON_ID, actualPatron.getId());
    }

    private void thenActualPatronHasExpectedCard() {
        assertEquals(LIBRARY_CARD, actualPatron.getLibraryCard());
    }

    private void thenActualPatronHasExpectedPin() {
        assertEquals(PIN, actualPatron.getPin());
    }

    @Test
    public void checkOutTestActiveLoan() {
        givenCheckOutTestResponseFromAlmaWithStatus(CheckOutTestErrorStatusType.ACTIVE_LOAN);
        whenCheckOutTest();
        thenPreCheckOutAnalysisReturnsActiveLoan();
    }

    @Test
    public void checkOutTestCheckoutDenied() {
        givenCheckOutTestResponseFromAlmaWithStatus(CheckOutTestErrorStatusType.CHECK_OUT_DENIED);
        try {
            whenCheckOutTest();
            fail();
        } catch (ForbiddenException ex) {
            thenForbiddenExceptionIsThrown(ex);
        }
    }

    @Test
    public void checkOutTestInvalidRecordId() {
        givenCheckOutTestResponseFromAlmaWithStatus(CheckOutTestErrorStatusType.INVALID_RECORD_ID);
        try {
            whenCheckOutTest();
            fail();
        } catch (NotFoundException ex) {
            thenNotFoundExceptionIsThrown(ex);
        }
    }


    @Test
    public void checkOutTestNewLoan() {
        givenCheckOutTestResponseFromAlmaWithStatus(CheckOutTestErrorStatusType.NEW_LOAN);
        whenCheckOutTest();
        thenPreCheckOutAnalysisReturnsNewLoan();
    }

    @Test
    public void checkOutSuccess() {
        givenCheckOutResponseWithStatus(null);
        whenCheckOut();
        thenLmsLoanSuccess();
    }

    @Test
    public void checkOutDenied() {
        givenCheckOutResponseWithStatus(CheckOutErrorStatusType.CHECK_OUT_DENIED);
        try {
            whenCheckOut();
            fail();
        } catch (ForbiddenException ex) {
            thenForbiddenExceptionIsThrown(ex);
        }
    }

    @Test
    public void checkOutInvalidRecordId() {
        givenCheckOutResponseWithStatus(CheckOutErrorStatusType.INVALID_RECORD_ID);
        try {
            whenCheckOut();
            fail();
        } catch (NotFoundException ex) {
            thenNotFoundExceptionIsThrown(ex);
        }
    }

    @Before
    public void setUp() throws Exception {
        underTest = new PalmaDataAccessor();
        ReflectionTestUtils.setField(underTest, "loansFacade", loansFacade);
        ReflectionTestUtils.setField(underTest, "patronFacade", patronFacade);
        ReflectionTestUtils.setField(underTest, "responseStatusChecker", new ResponseStatusChecker());
        pendingLoan = new PendingLoan(LMS_RECORD_ID, ContentProviderName.ELIB.name(), PROVIDER_RECORD_ID, PROVIDER_FORMAT_ID);
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperies = new HashMap<>();
        ehubConsumerProperies.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, PALMA_URL);
        ehubConsumerProperies.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, AGENCY_M_IDENTIFIER);
        given(patron.getLibraryCard()).willReturn(LIBRARY_CARD);
        given(patron.getPin()).willReturn(PIN);
        given(ehubConsumer.getProperties()).willReturn(ehubConsumerProperies);
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
    }

    private void givenCheckOutResponseWithStatus(final CheckOutErrorStatusType checkOutStatus) {
        CheckOutResponse checkOutResponse = getCheckOutResponse(checkOutStatus);
        given(loansFacade.checkOut(any(EhubConsumer.class), any(PendingLoan.class), any(Date.class), any(Patron.class))).willReturn(checkOutResponse);
    }

    private void thenLmsLoanSuccess() {
        assertNotNull(lmsLoan);
        assertEquals(LOAN_ID, lmsLoan.getId());
    }

    private void thenPreCheckOutAnalysisReturnsActiveLoan() {
        assertNotNull(preCheckoutAnalysis);
        assertEquals(CheckoutTestAnalysis.Result.ACTIVE_LOAN, preCheckoutAnalysis.getResult());
        assertEquals(LOAN_ID, preCheckoutAnalysis.getLmsLoanId());
    }

    private void thenPreCheckOutAnalysisReturnsNewLoan() {
        assertNotNull(preCheckoutAnalysis);
        assertEquals(CheckoutTestAnalysis.Result.NEW_LOAN, preCheckoutAnalysis.getResult());
        assertNull(preCheckoutAnalysis.getLmsLoanId());
    }

    private void thenForbiddenExceptionIsThrown(final ForbiddenException ex) {
        assertNotNull(ex.getMessage());
    }

    private void thenNotFoundExceptionIsThrown(final NotFoundException ex) {
        assertNotNull(ex.getMessage());
    }

    private static CheckOutTestResponse getCheckOutTestResponse(final CheckOutTestErrorStatusType checkOutStatus) {
        com.axiell.arena.services.palma.loans.ObjectFactory loansObjectFactory = new com.axiell.arena.services.palma.loans.ObjectFactory();
        CheckOutTestResponse checkOutTest = loansObjectFactory.createCheckOutTestResponse();
        com.axiell.arena.services.palma.util.status.ObjectFactory statusObjectFactory = new com.axiell.arena.services.palma.util.status.ObjectFactory();
        com.axiell.arena.services.palma.util.status.Status status = statusObjectFactory.createStatus();
        status.setType("ok");
        com.axiell.arena.services.palma.patron.checkouttestresponse.ObjectFactory checkouttestresponseObjectFactory =
                new com.axiell.arena.services.palma.patron.checkouttestresponse.ObjectFactory();
        com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse checkOutTestResponse =
                checkouttestresponseObjectFactory.createCheckOutTestResponse();
        checkOutTest.setCheckOutTestResponse(checkOutTestResponse);
        checkOutTestResponse.setStatus(status);
        switch (checkOutStatus) {
            case ACTIVE_LOAN:
                checkOutTestResponse.setLoanId(LOAN_ID);
                break;
            default:
        }
        checkOutTestResponse.setTestStatus(checkOutStatus);
        return checkOutTest;
    }

    private static CheckOutResponse getCheckOutResponse(final CheckOutErrorStatusType errorStatus) {
        com.axiell.arena.services.palma.loans.ObjectFactory loansObjectFactory = new com.axiell.arena.services.palma.loans.ObjectFactory();
        CheckOutResponse checkOut = loansObjectFactory.createCheckOutResponse();
        com.axiell.arena.services.palma.util.status.ObjectFactory statusObjectFactory = new com.axiell.arena.services.palma.util.status.ObjectFactory();
        com.axiell.arena.services.palma.util.status.Status status = statusObjectFactory.createStatus();
        status.setType("ok");
        com.axiell.arena.services.palma.patron.checkoutresponse.ObjectFactory checkoutresponseObjectFactory =
                new com.axiell.arena.services.palma.patron.checkoutresponse.ObjectFactory();
        com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse checkOutResponse = checkoutresponseObjectFactory.createCheckOutResponse();
        checkOut.setCheckOutResponse(checkOutResponse);
        checkOutResponse.setStatus(status);
        if (errorStatus == null) {
            com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse.CheckOutSuccess checkOutSuccess =
                    checkoutresponseObjectFactory.createCheckOutResponseCheckOutSuccess();
            checkOutSuccess.setLoanId(LOAN_ID);
            checkOutSuccess.setRecordId(LMS_RECORD_ID);
            com.axiell.arena.services.palma.util.ObjectFactory utilObjectFactory = new com.axiell.arena.services.palma.util.ObjectFactory();
            com.axiell.arena.services.palma.util.MoneyType fee = utilObjectFactory.createMoneyType();
            fee.setCurrency(ISOCurrencyCodeType.SEK);
            fee.setAmount(10);
            checkOutSuccess.setFee(fee);
            checkOutResponse.setCheckOutSuccess(checkOutSuccess);
        } else {
            com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse.CheckOutError checkOutError =
                    checkoutresponseObjectFactory.createCheckOutResponseCheckOutError();
            checkOutError.setStatus(errorStatus);
            checkOutResponse.setCheckOutError(checkOutError);
        }

        return checkOut;
    }

    private void givenCheckOutTestResponseFromAlmaWithStatus(final CheckOutTestErrorStatusType testStatus) {
        CheckOutTestResponse checkOutTestResponse = getCheckOutTestResponse(testStatus);
        given(loansFacade.checkOutTest(any(EhubConsumer.class), any(PendingLoan.class), any(Patron.class))).willReturn(checkOutTestResponse);
    }

    private void whenCheckOutTest() {
        preCheckoutAnalysis = underTest.checkoutTest(ehubConsumer, pendingLoan, patron);
    }

    private void whenCheckOut() {
        Date expirationDate = new Date();
        lmsLoan = underTest.checkout(ehubConsumer, pendingLoan, expirationDate, patron);
    }
}
