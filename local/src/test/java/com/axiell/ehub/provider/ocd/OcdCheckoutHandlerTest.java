package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.CommandData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class OcdCheckoutHandlerTest {
    private static final String RECORD_ID = "recordId";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String PATRON_ID = "patronId";
    private static final String CARD = "card";
    private static final String PIN = "pin";
    private OcdCheckoutHandler underTest;
    @Mock
    private IOcdFacade ocdFacade;
    @Mock
    private CommandData commandData;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    private String contentProviderLoanId;
    @Mock
    private CheckoutDTO checkoutDTO;
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;

    private Checkout actualCheckout;
    private Patron patron = new Patron.Builder().libraryCard(CARD).pin(PIN).build();

    @Before
    public void setUpUnderTest() {
        underTest = new OcdCheckoutHandler();
        ReflectionTestUtils.setField(underTest, "ocdFacade", ocdFacade);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void checkout() {
        givenPatronInCommandData();
        givenContentProviderInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenTransactionIdInCheckoutDTO();
        givenCheckoutDTOFromOcdFacade();
        givenPatronDTOFromOcdFacade();
        whenCheckout();
        thenActualTransactionIdEqualsExpectedTransactionId();
    }

    @Test
    public void getCheckout_success() {
        givenPatronInCommandData();
        givenContentProviderInCommandData();
        givenTransactionIdAsContentProviderLoanId();
        givenTransactionIdInCheckoutDTO();
        givenGetCheckoutDTOFromOcdFacade();
        givenPatronDTOFromOcdFacade();
        whenGetCompleteCheckout();
        thenActualTransactionIdEqualsExpectedTransactionId();
    }

    @Test(expected = InternalServerErrorException.class)
    public void getCheckout_checkoutNotFound() {
        givenPatronInCommandData();
        givenContentProviderInCommandData();
        givenTransactionIdAsContentProviderLoanId();
        givenPatronDTOFromOcdFacade();
        givenInternalServerErrorExceptionFromEhubExceptionFactory();
        whenGetCompleteCheckout();
    }

    public void givenTransactionIdInCheckoutDTO() {
        given(checkoutDTO.getTransactionId()).willReturn(TRANSACTION_ID);
    }

    public void givenPatronDTOFromOcdFacade() {
        given(ocdFacade.getOrCreatePatron(any(ContentProviderConsumer.class), any(Patron.class))).willReturn(PATRON_ID);
    }

    public void whenCheckout() {
        actualCheckout = underTest.checkout(commandData);
    }

    public void thenActualTransactionIdEqualsExpectedTransactionId() {
        assertEquals(TRANSACTION_ID, actualCheckout.getTransactionId());
    }


    public void givenTransactionIdAsContentProviderLoanId() {
        contentProviderLoanId = TRANSACTION_ID;
    }

    public void givenGetCheckoutDTOFromOcdFacade() {
        given(ocdFacade.getCheckout(any(ContentProviderConsumer.class), anyString(), anyString())).willReturn(checkoutDTO);
    }

    public void givenCheckoutDTOFromOcdFacade() {
        given(ocdFacade.checkout(any(ContentProviderConsumer.class), anyString(), anyString())).willReturn(checkoutDTO);
    }

    public void givenContentProviderInCommandData() {
        given(commandData.getContentProviderConsumer()).willReturn(contentProviderConsumer);
    }

    public void givenContentProviderRecordIdInCommandData() {
        given(commandData.getContentProviderRecordId()).willReturn(RECORD_ID);
    }

    public void whenGetCompleteCheckout() {
        actualCheckout = underTest.getCheckout(commandData, contentProviderLoanId);
    }


    public void givenInternalServerErrorExceptionFromEhubExceptionFactory() {
        given(ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(any(ContentProviderConsumer.class),
                any(ErrorCauseArgumentType.class), anyString())).willThrow(internalServerErrorException);
    }

    protected void givenPatronInCommandData() {
        given(commandData.getPatron()).willReturn(patron);
    }
}
