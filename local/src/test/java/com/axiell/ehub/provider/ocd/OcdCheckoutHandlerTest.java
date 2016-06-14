package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.CommandData;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class OcdCheckoutHandlerTest {
    public static final String TRANSACTION_ID = "transactionId";
    private OcdCheckoutHandler underTest;
    @Mock
    private IOcdFacade ocdFacade;
    @Mock
    private BearerToken bearerToken;
    @Mock
    private CommandData commandData;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    private String contentProviderLoanId;
    @Mock
    private CheckoutDTO checkoutDTO;
    private List<CheckoutDTO> checkoutDTOs;
    private Checkout actualCheckout;

    @Before
    public void setUpUnderTest() {
        underTest = new OcdCheckoutHandler();
        ReflectionTestUtils.setField(underTest, "ocdFacade", ocdFacade);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void checkout() {
        givenTransactionIdInCheckoutDTO();
        givenCheckoutDTOFromOcdFacade();
        whenCheckout();
        thenActualTransactionIdEqualsExpectedTransactionId();
    }

    public void givenTransactionIdInCheckoutDTO() {
        given(checkoutDTO.getTransactionId()).willReturn(TRANSACTION_ID);
    }

    public void givenCheckoutDTOFromOcdFacade() {
        given(ocdFacade.checkout(any(ContentProviderConsumer.class), any(BearerToken.class), anyString())).willReturn(checkoutDTO);
    }

    public void whenCheckout() {
        actualCheckout = underTest.checkout(bearerToken, commandData);
    }

    public void thenActualTransactionIdEqualsExpectedTransactionId() {
        assertEquals(TRANSACTION_ID, actualCheckout.getTransactionId());
    }

    @Test
    public void getCompleteCheckout_success() {
        givenTransactionIdAsContentProviderLoanId();
        givenTransactionIdInCheckoutDTO();
        givenListOfCheckoutDTOsFromOcdFacade();
        whenGetCompleteCheckout();
        thenActualTransactionIdEqualsExpectedTransactionId();
    }

    public void givenTransactionIdAsContentProviderLoanId() {
        contentProviderLoanId = TRANSACTION_ID;
    }

    public void givenListOfCheckoutDTOsFromOcdFacade() {
        checkoutDTOs = Lists.newArrayList(checkoutDTO);
        given(ocdFacade.getCheckouts(any(ContentProviderConsumer.class), any(BearerToken.class))).willReturn(checkoutDTOs);
    }

    public void whenGetCompleteCheckout() {
        actualCheckout = underTest.getCompleteCheckout(bearerToken, commandData, contentProviderLoanId);
    }

    @Test(expected = InternalServerErrorException.class)
    public void getCompleteCheckout_checkoutNotFound() {
        givenTransactionIdAsContentProviderLoanId();
        givenListOfCheckoutDTOsFromOcdFacade();
        givenInternalServerErrorExceptionFromEhubExceptionFactory();
        whenGetCompleteCheckout();
    }

    public void givenInternalServerErrorExceptionFromEhubExceptionFactory() {
        given(ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(any(ContentProviderConsumer.class), any(ErrorCauseArgumentType.class), anyString())).willThrow(internalServerErrorException);
    }
}
