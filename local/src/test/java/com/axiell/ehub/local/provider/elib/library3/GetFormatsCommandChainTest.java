package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.record.format.IFormatFactory;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetFormatsCommandChainTest {
    private static final String CP_RECORD_ID = "CP_RECORD_ID";
    private static final String FORMAT_ID = "FORMAT_ID";
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
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

    @BeforeEach
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
        Assertions.assertFalse(actualFormatSet.isEmpty());
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
        given(commandData.getLanguage()).willReturn(LANGUAGE);
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
        Assertions.assertTrue(actualFormatSet.isEmpty());
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
