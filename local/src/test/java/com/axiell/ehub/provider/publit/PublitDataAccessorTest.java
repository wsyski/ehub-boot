package com.axiell.ehub.provider.publit;

import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.publit.ShopOrderUrl.DownloadItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class PublitDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "1";
    private static final String FORMAT_ID = "formatId";
    private static final Integer SHOP_CUSTOMER_ORDER_ID = 1;

    private PublitDataAccessor underTest;
    @Mock
    private IPublitFacade publitFacade;
    @Mock
    private Product product;
    @Mock
    private ShopCustomerOrder shopCustomerOrder;
    @Mock
    private ShopOrderUrl shopOrderUrl;
    @Mock
    private DownloadItem downloadItem;

    @Before
    public void setUpPublitDataAccessor() {
        underTest = new PublitDataAccessor();
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "publitFacade", publitFacade);
        ReflectionTestUtils.setField(underTest, "expirationDateFactory", expirationDateFactory);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
    }

    private void givenProducts() {
        List<Product> products = Arrays.asList(product);
        given(publitFacade.getProduct(contentProviderConsumer, RECORD_ID)).willReturn(products);
    }

    private void givenFormatId() {
        given(product.getType()).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    @Test
    public void getFormatsWithEhubFormatNameAndDescription() {
        givenFormatFromFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenProducts();
        givenContentProvider();
        givenFormatId();
        givenTextBundle();
        whenGetFormats();
        thenActualFormatEqualsExpected();
    }

    @Test
    public void getFormatsWhenClientResponseFailureIsThrown() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenClientResponseFailureInGetProduct();
        givenClientResponse();
        givenClientResponseStatus();
        try {
            whenGetFormats();
        } catch (InternalServerErrorException e) {
            EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenClientResponseFailureInGetProduct() {
        given(publitFacade.getProduct(contentProviderConsumer, RECORD_ID)).willThrow(failure);
    }

    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronIdInPatron();
        givenPatronInCommandData();
        givenShopCustomerOrder();
        givenShopCustomerOrderId();
        givenShopOrderUrl();
        givenDownloadItems();
        givenDownloadUrl();
        givenExpirationDate();
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
        givenDownloadableContentDisposition();
        givenCreatedDownloadableContent();
        whenCreateLoan();
        thenActualLoanContainsDownloadUrl();
        thenGetIdFromPatronIsInvoked();
        thenGetLibraryCardFromPatronIsNeverInvoked();
    }

    private void thenGetLibraryCardFromPatronIsNeverInvoked() {
        verify(patron, never()).getLibraryCard();
    }

    private void thenGetIdFromPatronIsInvoked() {
        verify(patron, times(1)).getId();
    }

    private void givenShopCustomerOrder() {
        given(publitFacade.createShopOrder(contentProviderConsumer, RECORD_ID, PATRON_ID)).willReturn(shopCustomerOrder);
    }

    private void givenShopCustomerOrderId() {
        given(shopCustomerOrder.getId()).willReturn(SHOP_CUSTOMER_ORDER_ID);
    }

    private void givenShopOrderUrl() {
        given(publitFacade.getShopOrderUrl(contentProviderConsumer, SHOP_CUSTOMER_ORDER_ID.toString())).willReturn(shopOrderUrl);
    }

    private void givenDownloadItems() {
        List<DownloadItem> downloadItems = Arrays.asList(downloadItem);
        given(shopOrderUrl.getDownloadItems()).willReturn(downloadItems);
    }

    private void givenDownloadUrl() {
        given(downloadItem.getUrl()).willReturn(DOWNLOAD_URL);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    @Test
    public void createLoanWhenClientResponseFailureIsThrown() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronIdInPatron();
        givenPatronInCommandData();
        givenClientResponseFailureInCreateShopOrder();
        givenClientResponse();
        givenClientResponseStatus();
        try {
            whenCreateLoan();
        } catch (InternalServerErrorException e) {
            EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
        }
        thenGetIdFromPatronIsInvoked();
        thenGetLibraryCardFromPatronIsNeverInvoked();
    }

    private void givenClientResponseFailureInCreateShopOrder() {
        given(publitFacade.createShopOrder(contentProviderConsumer, RECORD_ID, PATRON_ID)).willThrow(failure);
    }

    @Test
    public void getContent() {
        givenContentProviderConsumerInCommandData();
        givenLibraryCardInPatron();
        givenContentProviderLoanMetadataInCommandData();
        givenContentProviderLoanId();
        givenShopOrderUrl();
        givenDownloadItems();
        givenDownloadUrl();
        givenContentProvider();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenDownloadableContentDisposition();
        givenCreatedDownloadableContent();
        whenGetContent();
        thenActualContentContainsDownloadUrl();
    }

    private void givenContentProviderLoanId() {
        given(loanMetadata.getId()).willReturn(SHOP_CUSTOMER_ORDER_ID.toString());
    }

    private void whenGetContent() {
        actualContent = underTest.getContent(commandData);
    }

    @Test
    public void getContentWhenEmptyListOfDownloadItems() {
        givenContentProviderConsumerInCommandData();
        givenLibraryCardInPatron();
        givenContentProviderLoanMetadataInCommandData();
        givenContentProviderLoanId();
        givenShopOrderUrl();
        givenEmptyListOfDownloadItems();
        try {
            whenGetContent();
        } catch (InternalServerErrorException e) {
            EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenEmptyListOfDownloadItems() {
        List<DownloadItem> downloadItems = new ArrayList<>();
        given(shopOrderUrl.getDownloadItems()).willReturn(downloadItems);
    }

    @Test
    public void getContentWhenClientResponseFailureIsThrown() {
        givenContentProviderConsumerInCommandData();
        givenLibraryCardInPatron();
        givenContentProviderLoanMetadataInCommandData();
        givenContentProviderLoanId();
        givenClientResponseFailureInGetShopOrderUrl();
        givenClientResponse();
        givenClientResponseStatus();
        try {
            whenGetContent();
        } catch (InternalServerErrorException e) {
            EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenClientResponseFailureInGetShopOrderUrl() {
        given(publitFacade.getShopOrderUrl(contentProviderConsumer, SHOP_CUSTOMER_ORDER_ID.toString())).willThrow(failure);
    }
}
