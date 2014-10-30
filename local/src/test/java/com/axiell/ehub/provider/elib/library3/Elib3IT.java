package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.List;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_KEY;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static junit.framework.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class Elib3IT extends AbstractContentProviderIT {
    private static final String API_BASE_URL_VALUE = "https://webservices.elib.se/library/v3.0";
    private static final String ELIB_SERVICE_ID_VALUE = "1873";
    private static final String ELIB_SERVICE_KEY_VALUE = "Vm3qh9eZijFdMDxAEpn5PzyfC0S4sBGOvXtJrIYw1Ukl8cuoR2";
    private static final String EBOOK_PRODUCT_ID = "1002446";
    private static final String AUDIOBOOK_PRODUCT_ID = "1023509";
    private static final String ELIB_LOAN_ID_VALUE = "4802146";
    private static final String HTML5_FORMAT_ID = "4101";
    private static final String FLASH_FORMAT_ID = "4002";
    private static final String PATRON_ID_0 = "0";
    private static final String PATRON_ID_1 = "1";
    private String expectedFormatId;
    private String productId;
    private Elib3Facade underTest;
    @Mock
    private Patron patronWithoutLoans;
    @Mock
    private Patron patronWithLoans;
    private Patron patron;
    private BookAvailability bookAvailability;
    private Product product;
    private CreatedLoan createdLoan;
    private Loan loan;
    private LibraryProduct libraryProduct;
    private GetLoansResponse getLoansResponse;

    @Before
    public void setElibFacade() {
        underTest = new Elib3Facade();
    }

    @Before
    public void setUpPatronWithoutLoans() {
        given(patronWithoutLoans.hasId()).willReturn(true);
        given(patronWithoutLoans.getId()).willReturn(PATRON_ID_0);
    }

    @Before
    public void setUpPatronWithLoans() {
        given(patronWithLoans.hasId()).willReturn(true);
        given(patronWithLoans.getId()).willReturn(PATRON_ID_1);
    }

    @Test
    public void getBookAvailability() {
        givenApiBaseUrl();
        givenElibCredentials();
        whenGetBookAvailability();
        thenBookAvailabilityResponseContainsExpectedProduct();
    }

    @Test
    public void getProduct_ebook() {
        givenApiBaseUrl();
        givenElibCredentials();
        givenEbookProductId();
        givenHtml5AsExpectedFormat();
        whenGetProduct();
        thenProductIsNotNull();
        thenProductHasEbookProductId();
        thenProductHasExpectedFormat();
    }

    private void givenHtml5AsExpectedFormat() {
        expectedFormatId = HTML5_FORMAT_ID;
    }

    private void givenEbookProductId() {
        productId = EBOOK_PRODUCT_ID;
    }

    @Test
    public void getProduct_audiobook() {
        givenApiBaseUrl();
        givenElibCredentials();
        givenAudiobookProductId();
        givenFlashAsExpectedFormat();
        whenGetProduct();
        thenProductIsNotNull();
        thenProductHasAudiobookProductId();
        thenProductHasExpectedFormat();
    }

    private void givenFlashAsExpectedFormat() {
        expectedFormatId = FLASH_FORMAT_ID;
    }

    private void thenProductHasExpectedFormat() {
        List<Product.AvailableFormat> formats = product.getFormats();
        assertFalse(formats.isEmpty());

        Iterator<Product.AvailableFormat> itr = formats.iterator();
        boolean formatFound = false;
        while (itr.hasNext() && !formatFound) {
            Product.AvailableFormat format = itr.next();
            formatFound = expectedFormatId.equals(format.getId());
        }
        assertTrue(formatFound);
    }

    private void givenAudiobookProductId() {
        productId = AUDIOBOOK_PRODUCT_ID;
    }

    @Test
    public void createLoan() {
        givenApiBaseUrl();
        givenElibCredentials();
        whenCreateLoan();
        thenCreatedLoanIsNotNull();
        thenCreatedLoanHasExpectedProductId();
        thenCreatedLoanHasContent();
    }

    @Test
    public void getLoan() {
        givenApiBaseUrl();
        givenElibCredentials();
        whenGetLoan();
        thenRetrievedLoanIsNotNull();
        thenRetrievedLoanHasExpectedLoanId();
        thenRetrievedLoanHasContentIfLoanIsActive();
    }

    @Test
    public void getLibraryProduct() {
        givenApiBaseUrl();
        givenElibCredentials();
        whenGetLibraryProduct();
        thenLibraryProductIsNotNull();
        thenLibraryProductContainsExpectedProductId();
        thenLibraryProductHasAvailableModel();
    }

    private void thenProductHasEbookProductId() {
        assertEquals(EBOOK_PRODUCT_ID, product.getProductId());
    }

    private void thenProductHasAudiobookProductId() {
        assertEquals(AUDIOBOOK_PRODUCT_ID, product.getProductId());
    }

    private void thenCreatedLoanHasExpectedProductId() {
        assertEquals(EBOOK_PRODUCT_ID, createdLoan.getProductId());
    }

    private void thenProductIsNotNull() {
        assertNotNull(product);
    }

    private void thenRetrievedLoanIsNotNull() {
        assertNotNull(loan);
    }

    private void thenRetrievedLoanHasExpectedLoanId() {
        assertEquals(ELIB_LOAN_ID_VALUE, loan.getLoanId());
    }

    private void thenRetrievedLoanHasContentIfLoanIsActive() {
        if (loan.isActive())
            assertNotNull(loan.getContentUrlFor(HTML5_FORMAT_ID));
    }

    private void whenGetLibraryProduct() {
        libraryProduct = underTest.getLibraryProduct(contentProviderConsumer, EBOOK_PRODUCT_ID);
    }

    private void thenLibraryProductIsNotNull() {
        assertNotNull(libraryProduct);
    }

    private void thenLibraryProductContainsExpectedProductId() {
        assertEquals(EBOOK_PRODUCT_ID, libraryProduct.getProductId());
    }

    private void thenCreatedLoanHasContent() {
        assertNotNull(createdLoan.getContentUrlFor(HTML5_FORMAT_ID));
    }

    private void thenLibraryProductHasAvailableModel() {
        assertTrue(libraryProduct.hasAvailableModel());
    }

    private void givenApiBaseUrl() {
        givenContentProvider();
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(API_BASE_URL_VALUE);
    }

    private void givenElibCredentials() {
        given(contentProviderConsumer.getProperty(ELIB_SERVICE_ID)).willReturn(ELIB_SERVICE_ID_VALUE);
        given(contentProviderConsumer.getProperty(ELIB_SERVICE_KEY)).willReturn(ELIB_SERVICE_KEY_VALUE);
    }

    private void whenGetBookAvailability() {
        bookAvailability = underTest.getBookAvailability(contentProviderConsumer, EBOOK_PRODUCT_ID, patronWithoutLoans);
    }

    private void thenBookAvailabilityResponseContainsExpectedProduct() {
        assertNotNull(bookAvailability);
        final List<BookAvailability.Product> products = bookAvailability.getProducts();
        assertFalse(products.isEmpty());
        final boolean expectedProductFound = findExpectedProduct(products);
        assertTrue(expectedProductFound);
    }

    private boolean findExpectedProduct(List<BookAvailability.Product> products) {
        boolean expectedProductFound = false;
        for (BookAvailability.Product product : products) {
            final String productId = product.getProductId();
            if (EBOOK_PRODUCT_ID.equals(productId))
                expectedProductFound = true;
        }
        return expectedProductFound;
    }

    private void whenGetProduct() {
        product = underTest.getProduct(contentProviderConsumer, productId);
    }

    private void whenGetLoan() {
        loan = underTest.getLoan(contentProviderConsumer, ELIB_LOAN_ID_VALUE);
    }

    private void whenCreateLoan() {
        createdLoan = underTest.createLoan(contentProviderConsumer, EBOOK_PRODUCT_ID, patronWithoutLoans);
    }

    private void thenCreatedLoanIsNotNull() {
        assertNotNull(createdLoan);
    }

    @Test
    public void getLoans_hasLoans() {
        givenApiBaseUrl();
        givenElibCredentials();
        givenPatronWithLoans();
        whenGetLoans();
        thenGetLoansResponseIsNotNull();
        thenLoanWithExpectedProductIdExists();
        thenRetrievedLoanHasContentIfLoanIsActive();
        thenLoanExpirationDateIsNotNull();
    }

    private void thenLoanExpirationDateIsNotNull() {
        assertNotNull(loan.getExpirationDate());
    }

    private void givenPatronWithLoans() {
        patron = patronWithLoans;
    }

    private void thenGetLoansResponseIsNotNull() {
        assertNotNull(getLoansResponse);
    }

    private void thenLoanWithExpectedProductIdExists() {
        assertNotNull(loan);
    }

    private void whenGetLoans() {
        getLoansResponse = underTest.getLoans(contentProviderConsumer, patron);
        loan = getLoansResponse.getLoanWithProductId(EBOOK_PRODUCT_ID);
    }

    @Test
    public void getLoans_noLoans() {
        givenApiBaseUrl();
        givenElibCredentials();
        givenPatronWithoutLoans();
        whenGetLoans();
        thenGetLoansResponseIsNotNull();
        thenLoanWithExpectedProductIdDoesNotExist();
    }

    private void givenPatronWithoutLoans() {
        patron = patronWithoutLoans;
    }

    private void thenLoanWithExpectedProductIdDoesNotExist() {
        assertNull(loan);
    }
}
