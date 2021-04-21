package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse;
import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutErrorStatusType;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestErrorStatusType;
import com.axiell.arena.services.palma.search.v267.searchresponse.CatalogueRecords;
import com.axiell.arena.services.palma.search.v267.service.SearchResponse;
import com.axiell.arena.services.palma.util.ISOCurrencyCodeType;
import com.axiell.arena.services.palma.util.status.Status;
import com.axiell.arena.services.palma.util.v267.cr.CatalogueRecord;
import com.axiell.ehub.FieldsBuilder;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.authinfo.Patron;

import com.axiell.ehub.provider.ContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PalmaDataAccessorTest {
    private static final String MEDIA_CLASS = "eAudio";
    private static final String LMS_RECORD_ID = "recordId";
    private static final String PROVIDER_RECORD_ID = "providerRecordId";
    private static final String LIBRARY_CARD = "libraryCard";
    private static final String PIN = "pin";
    private static final String LOAN_ID = "loanId";
    private static final String PALMA_URL = "http://localhost:16521/ehub.pa.palma";
    private static final String AGENCY_M_IDENTIFIER = "agencyMemberIdentifier";
    private static final long EHUB_CONSUMER_ID = 1L;

    private static final com.axiell.arena.services.palma.search.v267.searchresponse.ObjectFactory SEARCH_RESPONSE_OBJECT_FACTORY =
            new com.axiell.arena.services.palma.search.v267.searchresponse.ObjectFactory();
    private static final com.axiell.arena.services.palma.search.v267.service.ObjectFactory SEARCH_SERVICE_OBJECT_FACTORY =
            new com.axiell.arena.services.palma.search.v267.service.ObjectFactory();
    private static final com.axiell.arena.services.palma.util.v267.cr.ObjectFactory CR_OBJECT_FACTORY =
            new com.axiell.arena.services.palma.util.v267.cr.ObjectFactory();
    private static final com.axiell.arena.services.palma.util.v267.status.ObjectFactory V267_STATUS_OBJECT_FACTORY =
            new com.axiell.arena.services.palma.util.v267.status.ObjectFactory();
    private static final com.axiell.arena.services.palma.patron.checkoutresponse.ObjectFactory CHECKOUT_RESPONSE_OBJECT_FACTORY = new com.axiell.arena.services.palma.patron.checkoutresponse.ObjectFactory();
    private static final com.axiell.arena.services.palma.patron.checkouttestresponse.ObjectFactory CHECKOUT_TEST_RESPONSE_OBJECT_FACTORY = new com.axiell.arena.services.palma.patron.checkouttestresponse.ObjectFactory();
    private static final com.axiell.arena.services.palma.util.status.ObjectFactory STATUS_OBJECT_FACTORY =
            new com.axiell.arena.services.palma.util.status.ObjectFactory();

    private PalmaDataAccessor underTest;
    @Mock
    private ILoansFacade loansFacade;
    @Mock
    private ICatalogueFacade catalogueFacade;
    @Mock
    private EhubConsumer ehubConsumer;
    @Mock
    private Patron patron;

    private PendingLoan pendingLoan;
    private CheckoutTestAnalysis preCheckoutAnalysis;
    private LmsLoan lmsLoan;
    private String mediaClass;

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

    @Test
    public void getMediaClass() {
        givenPalmaSearch();
        whenGetMediaClass();
        thenExpectedMediaClass();
    }

    @Before
    public void setUp() throws Exception {
        underTest = new PalmaDataAccessor();
        ReflectionTestUtils.setField(underTest, "loansFacade", loansFacade);
        ReflectionTestUtils.setField(underTest, "catalogueFacade", catalogueFacade);
        ReflectionTestUtils.setField(underTest, "responseStatusChecker", new ResponseStatusChecker());
        pendingLoan = new PendingLoan(FieldsBuilder.defaultFields());
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperies = new HashMap<>();
        ehubConsumerProperies.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_LOCAL_API_ENDPOINT, PALMA_URL);
        ehubConsumerProperies.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, AGENCY_M_IDENTIFIER);
        given(patron.getLibraryCard()).willReturn(LIBRARY_CARD);
        given(patron.getPin()).willReturn(PIN);
        given(ehubConsumer.getProperties()).willReturn(ehubConsumerProperies);
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
    }

    private void givenPalmaSearch() {
        SearchResponse.SearchResult searchResult = getSearchResult();
        given(catalogueFacade.search(any(EhubConsumer.class), any(String.class), any(String.class))).willReturn(searchResult);
    }

    private void givenCheckOutResponseWithStatus(final CheckOutErrorStatusType checkOutStatus) {
        CheckOutResponse checkOutResponse = getCheckOutResponse(checkOutStatus);
        given(loansFacade.checkOut(any(EhubConsumer.class), any(PendingLoan.class), any(Date.class), any(Patron.class), any(boolean.class))).willReturn(checkOutResponse);
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

    private void thenExpectedMediaClass() {
        assertEquals(MEDIA_CLASS, mediaClass);
    }

    private void thenForbiddenExceptionIsThrown(final ForbiddenException ex) {
        assertNotNull(ex.getMessage());
    }

    private void thenNotFoundExceptionIsThrown(final NotFoundException ex) {
        assertNotNull(ex.getMessage());
    }

    private static CheckOutTestResponse getCheckOutTestResponse(final CheckOutTestErrorStatusType checkOutStatus) {
        CheckOutTestResponse checkOutTestResponse = CHECKOUT_TEST_RESPONSE_OBJECT_FACTORY.createCheckOutTestResponse();
        Status status = getStatusOk();
        checkOutTestResponse.setStatus(status);
        switch (checkOutStatus) {
            case ACTIVE_LOAN:
                checkOutTestResponse.setLoanId(LOAN_ID);
                //checkOutTestResponse.setLoanId(CHECKOUT_TEST_RESPONSE_OBJECT_FACTORY.createCheckOutTestResponseLoanId(LOAN_ID));
                break;
            default:
        }
        checkOutTestResponse.setTestStatus(checkOutStatus);
        return checkOutTestResponse;
    }

    private static CheckOutResponse getCheckOutResponse(final CheckOutErrorStatusType errorStatus) {
        CheckOutResponse checkOutResponse = CHECKOUT_RESPONSE_OBJECT_FACTORY.createCheckOutResponse();
        com.axiell.arena.services.palma.util.status.Status status = getStatusOk();
        checkOutResponse.setStatus(status);
        if (errorStatus == null) {
            com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse.CheckOutSuccess checkOutSuccess =
                    CHECKOUT_RESPONSE_OBJECT_FACTORY.createCheckOutResponseCheckOutSuccess();
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
                    CHECKOUT_RESPONSE_OBJECT_FACTORY.createCheckOutResponseCheckOutError();
            checkOutError.setStatus(errorStatus);
            checkOutResponse.setCheckOutError(checkOutError);
        }

        return checkOutResponse;
    }

    private static Status getStatusOk() {
        Status status = STATUS_OBJECT_FACTORY.createStatus();
        status.setType("ok");
        return status;
    }

    private static com.axiell.arena.services.palma.util.v267.status.Status getV267StatusOk() {
        com.axiell.arena.services.palma.util.v267.status.Status status = V267_STATUS_OBJECT_FACTORY.createStatus();
        status.setType("ok");
        return status;
    }

    private static SearchResponse.SearchResult getSearchResult() {
        com.axiell.arena.services.palma.util.v267.status.Status status = getV267StatusOk();
        SearchResponse.SearchResult searchResult = SEARCH_SERVICE_OBJECT_FACTORY.createSearchResponseSearchResult();
        searchResult.setStatus(status);
        CatalogueRecord catalogueRecord = CR_OBJECT_FACTORY.createCatalogueRecord();
        catalogueRecord.setMediaClass(MEDIA_CLASS);
        catalogueRecord.setId(LMS_RECORD_ID);
        CatalogueRecords catalogueRecords = SEARCH_RESPONSE_OBJECT_FACTORY.createCatalogueRecords();
        catalogueRecords.getCatalogueRecord().add(catalogueRecord);
        searchResult.setCatalogueRecords(catalogueRecords);
        searchResult.setNofPages(1);
        searchResult.setNofRecordsTotal(1);
        searchResult.setNofRecordsPage(1);
        searchResult.setCurrentPage(1);
        return searchResult;
    }

    private void givenCheckOutTestResponseFromAlmaWithStatus(final CheckOutTestErrorStatusType testStatus) {
        CheckOutTestResponse checkOutTestResponse = getCheckOutTestResponse(testStatus);
        given(loansFacade.checkOutTest(any(EhubConsumer.class), any(PendingLoan.class), any(Patron.class), any(boolean.class))).willReturn(checkOutTestResponse);
    }

    private void whenCheckOutTest() {
        preCheckoutAnalysis = underTest.checkoutTest(ehubConsumer, pendingLoan, patron, false, Locale.ENGLISH);
    }

    private void whenCheckOut() {
        Date expirationDate = new Date();
        lmsLoan = underTest.checkout(ehubConsumer, pendingLoan, expirationDate, patron, false, Locale.ENGLISH);
    }

    private void whenGetMediaClass() {
        mediaClass = underTest.getMediaClass(ehubConsumer, ContentProvider.CONTENT_PROVIDER_OCD, PROVIDER_RECORD_ID, Locale.ENGLISH);
    }
}
