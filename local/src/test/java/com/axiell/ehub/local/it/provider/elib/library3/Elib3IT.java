package com.axiell.ehub.local.it.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.local.it.provider.AbstractContentProviderIT;
import com.axiell.ehub.local.provider.elib.library3.BookAvailability;
import com.axiell.ehub.local.provider.elib.library3.CreatedLoan;
import com.axiell.ehub.local.provider.elib.library3.Elib3Facade;
import com.axiell.ehub.local.provider.elib.library3.GetLoansResponse;
import com.axiell.ehub.local.provider.elib.library3.LibraryProduct;
import com.axiell.ehub.local.provider.elib.library3.LoanDTO;
import com.axiell.ehub.local.provider.elib.library3.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Iterator;
import java.util.List;

import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_ID;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_KEY;
import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Disabled
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
    private LoanDTO loan;
    private LibraryProduct libraryProduct;
    private GetLoansResponse getLoansResponse;

    @BeforeEach
    public void setElibFacade() {
        underTest = new Elib3Facade();
    }

    @BeforeEach
    public void setUpPatronWithoutLoans() {
        given(patronWithoutLoans.hasId()).willReturn(true);
        given(patronWithoutLoans.getId()).willReturn(PATRON_ID_0);
    }

    @BeforeEach
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
        Assertions.assertFalse(formats.isEmpty());

        Iterator<Product.AvailableFormat> itr = formats.iterator();
        boolean formatFound = false;
        while (itr.hasNext() && !formatFound) {
            Product.AvailableFormat format = itr.next();
            formatFound = expectedFormatId.equals(format.getId());
        }
        Assertions.assertTrue(formatFound);
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
        Assertions.assertEquals(EBOOK_PRODUCT_ID, product.getProductId());
    }

    private void thenProductHasAudiobookProductId() {
        Assertions.assertEquals(AUDIOBOOK_PRODUCT_ID, product.getProductId());
    }

    private void thenCreatedLoanHasExpectedProductId() {
        Assertions.assertEquals(EBOOK_PRODUCT_ID, createdLoan.getProductId());
    }

    private void thenProductIsNotNull() {
        Assertions.assertNotNull(product);
    }

    private void thenRetrievedLoanIsNotNull() {
        Assertions.assertNotNull(loan);
    }

    private void thenRetrievedLoanHasExpectedLoanId() {
        Assertions.assertEquals(ELIB_LOAN_ID_VALUE, loan.getLoanId());
    }

    private void thenRetrievedLoanHasContentIfLoanIsActive() {
        if (loan.isActive()) {
            Assertions.assertEquals(1, loan.getContentUrlsFor(HTML5_FORMAT_ID).size());
            Assertions.assertNotNull(loan.getContentUrlsFor(HTML5_FORMAT_ID).get(0));
        }
    }

    private void whenGetLibraryProduct() {
        libraryProduct = underTest.getLibraryProduct(contentProviderConsumer, EBOOK_PRODUCT_ID);
    }

    private void thenLibraryProductIsNotNull() {
        Assertions.assertNotNull(libraryProduct);
    }

    private void thenLibraryProductContainsExpectedProductId() {
        Assertions.assertEquals(EBOOK_PRODUCT_ID, libraryProduct.getProductId());
    }

    private void thenCreatedLoanHasContent() {
        Assertions.assertEquals(1, createdLoan.getContentUrlsFor(HTML5_FORMAT_ID).size());
        Assertions.assertNotNull(createdLoan.getContentUrlsFor(HTML5_FORMAT_ID).get(0));
    }

    private void thenLibraryProductHasAvailableModel() {
        Assertions.assertTrue(libraryProduct.hasAvailableModel());
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
        Assertions.assertNotNull(bookAvailability);
        final List<BookAvailability.Product> products = bookAvailability.getProducts();
        Assertions.assertFalse(products.isEmpty());
        final boolean expectedProductFound = findExpectedProduct(products);
        Assertions.assertTrue(expectedProductFound);
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
        Assertions.assertNotNull(createdLoan);
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
        Assertions.assertNotNull(loan.getExpirationDate());
    }

    private void givenPatronWithLoans() {
        patron = patronWithLoans;
    }

    private void thenGetLoansResponseIsNotNull() {
        Assertions.assertNotNull(getLoansResponse);
    }

    private void thenLoanWithExpectedProductIdExists() {
        Assertions.assertNotNull(loan);
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
        Assertions.assertNull(loan);
    }
}
