package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.auth.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GetFormatsCommandChainTest {
    private static final String CP_RECORD_ID = "CP_RECORD_ID";
    private static final String FORMAT_ID = "FORMAT_ID";
    private GetFormatsCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private IFormatFactory formatFactory;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private BookAvailability bookAvailability;
    private Elib3CommandData elib3CommandData;
    @Mock
    private Product product;
    @Mock
    private Product.AvailableFormat availableFormat;
    @Mock
    private LibraryProduct libraryProduct;
    private Set<Format> actualFormatSet;
    @Mock
    private Format expFormat;
    @Mock
    private CommandData commandData;
    @Mock
    private Patron patron;
    @Mock
    private GetLoansResponse getLoansResponse;
    @Mock
    private LoanDTO loan;
    private Format actualFormat;

    @Before
    public void setUpUnderTest() {
        underTest = new GetFormatsCommandChain(elibFacade, exceptionFactory, formatFactory);
    }

    @Test
    public void makeFormats_notOnLoan_noAvailableFormats() {
        givenCommandData();
        givenActiveProduct();
        givenAvailableProduct();
        givenNoLoanForProductId();
        givenAvailableModel();
        whenExecute();
        thenActualFormatSetIsEmpty();
        thenBookAvailabilityIsInvoked();
        thenLibraryProductIsInvoked();
    }

    @Test
    public void makeFormats_notOnLoan_availableFormats() {
        givenCommandData();
        givenProductWithAvailableFormats();
        givenActiveProduct();
        givenAvailableProduct();
        givenNoLoanForProductId();
        givenAvailableModel();
        givenExpectedFormat();
        whenExecute();
        thenActualFormatSetIsNotEmpty();
        thenActualFormatEqualsExpectedFormat();
        thenBookAvailabilityIsInvoked();
        thenLibraryProductIsInvoked();
    }

    @Test
    public void makeFormats_onLoan_noAvailableFormats() {
        givenCommandData();
        givenActiveProduct();
        givenLoanForProductId();
        whenExecute();
        thenActualFormatSetIsEmpty();
        thenBookAvailabilityIsNotInvoked();
        thenLibraryProductIsNotInvoked();
    }

    @Test
    public void makeFormats_onLoan_availableFormats() {
        givenCommandData();
        givenProductWithAvailableFormats();
        givenActiveProduct();
        givenLoanForProductId();
        givenExpectedFormat();
        whenExecute();
        thenActualFormatSetIsNotEmpty();
        thenActualFormatEqualsExpectedFormat();
        thenBookAvailabilityIsNotInvoked();
        thenLibraryProductIsNotInvoked();
    }

    private void thenActualFormatEqualsExpectedFormat() {
        assertThat(actualFormat, is(expFormat));
    }

    private void thenActualFormatSetIsNotEmpty() {
        Assert.assertFalse(actualFormatSet.isEmpty());
    }

    private void givenExpectedFormat() {
        given(formatFactory.create(any(ContentProvider.class), any(String.class), any(String.class))).willReturn(expFormat);
    }

    private void givenCommandData() {
        given(patron.hasId()).willReturn(true);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(commandData.getContentProviderConsumer()).willReturn(contentProviderConsumer);
        given(commandData.getPatron()).willReturn(patron);
        given(commandData.getContentProviderRecordId()).willReturn("contentProviderRecordId");
        elib3CommandData = Elib3CommandData.newInstance(commandData);
    }

    private void givenProductWithAvailableFormats() {
        given(availableFormat.getId()).willReturn(FORMAT_ID);
        List<Product.AvailableFormat> availableFormats = Lists.newArrayList(availableFormat);
        given(product.getFormats()).willReturn(availableFormats);
    }

    private void givenActiveProduct() {
        given(product.isActive()).willReturn(true);
        given(elibFacade.getProduct(any(ContentProviderConsumer.class), any(String.class))).willReturn(product);
    }

    private void givenNoLoanForProductId() {
        givenGetLoansResponse();
    }

    private void givenGetLoansResponse() {
        given(elibFacade.getLoans(any(ContentProviderConsumer.class), any(Patron.class))).willReturn(getLoansResponse);
    }

    private void givenLoanForProductId() {
        given(loan.getExpirationDate()).willReturn(new Date());
        given(loan.getLoanId()).willReturn("loadId");
        given(loan.getProductId()).willReturn("productId");
        given(getLoansResponse.getLoanWithProductId(anyString())).willReturn(loan);
        givenGetLoansResponse();
    }

    private void givenAvailableModel() {
        given(libraryProduct.hasAvailableModel()).willReturn(true);
        given(elibFacade.getLibraryProduct(any(ContentProviderConsumer.class), any(String.class))).willReturn(libraryProduct);
    }

    private void whenExecute() {
        final List<Format> actualFormats = underTest.execute(elib3CommandData);
        actualFormatSet = new HashSet<>(actualFormats);
        Iterator<Format> itr = actualFormatSet.iterator();
        actualFormat = itr.hasNext() ? actualFormatSet.iterator().next() : null;
    }

    private void givenAvailableProduct() {
        given(bookAvailability.isProductAvailable(any(String.class))).willReturn(true);
        given(elibFacade.getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(Patron.class))).willReturn(bookAvailability);
    }

    private void thenActualFormatSetIsEmpty() {
        assertTrue(actualFormatSet.isEmpty());
    }

    private void thenLibraryProductIsInvoked() {
        verify(elibFacade).getLibraryProduct(any(ContentProviderConsumer.class), any(String.class));
    }

    private void thenLibraryProductIsNotInvoked() {
        verify(elibFacade, never()).getLibraryProduct(any(ContentProviderConsumer.class), any(String.class));
    }

    private void thenBookAvailabilityIsInvoked() {
        verify(elibFacade).getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(Patron.class));
    }

    private void thenBookAvailabilityIsNotInvoked() {
        verify(elibFacade, never()).getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(Patron.class));
    }
}
