package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.provider.AbstractContentProviderIT;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_KEY;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static junit.framework.Assert.*;
import static org.mockito.BDDMockito.given;

public class Elib3IT extends AbstractContentProviderIT {
    private static final String API_BASE_URL_VALUE = "https://webservices.elib.se/library/v3.0";
    private static final String ELIB_SERVICE_ID_VALUE = "1873";
    private static final String ELIB_SERVICE_KEY_VALUE = "Vm3qh9eZijFdMDxAEpn5PzyfC0S4sBGOvXtJrIYw1Ukl8cuoR2";
    private static final String ELIB_PRODUCT_ID_VALUE = "1002446";
    private static final String ELIB_LOAN_ID_VALUE = "4802146";
    private static final String LIBRARY_CARD = "1";
    private Elib3Facade underTest;

    private BookAvailability bookAvailability;
    private Product product;
    private CreatedLoan createdLoan;
    private Loan loan;
    private LibraryProduct libraryProduct;

    @Before
    public void setElibFacade() {
        underTest = new Elib3Facade();
    }

    @Test
    public void getBookAvailability() {
        givenApiBaseUrl();
        givenElibCredentials();
        whenGetBookAvailability();
        thenBookAvailabilityResponseContainsExpectedProduct();
    }

    @Test
    public void getProduct() {
        givenApiBaseUrl();
        givenElibCredentials();
        whenGetProduct();
        thenProductIsNotNull();
        thenProductHasExpectedProductId();
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

    private void thenProductHasExpectedProductId() {
        assertEquals(ELIB_PRODUCT_ID_VALUE, product.getProductId());
    }

    private void thenCreatedLoanHasExpectedProductId() {
        assertEquals(ELIB_PRODUCT_ID_VALUE, createdLoan.getProductId());
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
            assertNotNull(loan.getFirstContentUrl());
    }

    private void whenGetLibraryProduct() {
        libraryProduct = underTest.getLibraryProduct(contentProviderConsumer, ELIB_PRODUCT_ID_VALUE);
    }

    private void thenLibraryProductIsNotNull() {
        assertNotNull(libraryProduct);
    }

    private void thenLibraryProductContainsExpectedProductId() {
        assertEquals(ELIB_PRODUCT_ID_VALUE, libraryProduct.getProductId());
    }

    private void thenCreatedLoanHasContent() {
        assertNotNull(createdLoan.getFirstContentUrl());
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
        bookAvailability = underTest.getBookAvailability(contentProviderConsumer, ELIB_PRODUCT_ID_VALUE, LIBRARY_CARD);
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
            if (ELIB_PRODUCT_ID_VALUE.equals(productId))
                expectedProductFound = true;
        }
        return expectedProductFound;
    }

    private void whenGetProduct() {
        product = underTest.getProduct(contentProviderConsumer, ELIB_PRODUCT_ID_VALUE);
    }

    private void whenGetLoan() {
        loan = underTest.getLoan(contentProviderConsumer, ELIB_LOAN_ID_VALUE);
    }

    private void whenCreateLoan() {
        createdLoan = underTest.createLoan(contentProviderConsumer, ELIB_PRODUCT_ID_VALUE, LIBRARY_CARD);
    }

    private void thenCreatedLoanIsNotNull() {
        assertNotNull(createdLoan);
    }
}
