package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GetFormatsCommandChainTest {
    private static final String CP_RECORD_ID = "CP_RECORD_ID";
    private static final String FORMAT_ID = "FORMAT_ID";
    private static final String FORMAT_NAME = "FORMAT_NAME";
    private static final String FORMAT_DESC = "FORMAT_DESC";
    private GetFormatsCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private BookAvailability bookAvailability;
    private Elib3CommandData commandData;
    @Mock
    private Product product;
    @Mock
    private Product.AvailableFormat availableFormat;
    @Mock
    private LibraryProduct libraryProduct;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private FormatTextBundle textBundle;
    private Format actualFormat;

    @Before
    public void setUpUnderTest() {
        underTest = new GetFormatsCommandChain(elibFacade, exceptionFactory);
    }

    @Test
    public void getFormatsWhenNoAvailableTexts() {
        givenCommandData();
        givenActiveProductWithAvailableFormats();
        givenAvailableProduct();
        givenAvailableModel();
        whenExecute();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsFormatId();
        thenNameEqualsFormatId();
    }

    @Test
    public void getFormatsWhenAvailableTexts() {
        givenNameAndDescriptionInFormatTextBundle();
        givenCommandData();
        givenActiveProductWithAvailableFormats();
        givenAvailableProduct();
        givenAvailableModel();
        whenExecute();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsExpectedDescription();
        thenNameEqualsExpectedName();
    }

    private void givenNameAndDescriptionInFormatTextBundle() {
        given(textBundle.getName()).willReturn(FORMAT_NAME);
        given(textBundle.getDescription()).willReturn(FORMAT_DESC);
        given(formatDecoration.getTextBundle(any(String.class))).willReturn(textBundle);
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }

    private void thenDescriptionEqualsExpectedDescription() {
        assertThat(FORMAT_DESC, is(actualFormat.getDescription()));
    }

    private void thenNameEqualsExpectedName() {
        assertThat(FORMAT_NAME, is(actualFormat.getName()));
    }

    private void givenCommandData() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        commandData = Elib3CommandData.newInstance(contentProviderConsumer, "libraryCard", CP_RECORD_ID, "en");
    }

    private void givenActiveProductWithAvailableFormats() {
        given(availableFormat.getId()).willReturn(FORMAT_ID);
        List<Product.AvailableFormat> availableFormats = Lists.newArrayList(availableFormat);
        given(product.getFormats()).willReturn(availableFormats);
        given(product.isActive()).willReturn(true);
        given(elibFacade.getProduct(any(ContentProviderConsumer.class), any(String.class))).willReturn(product);
    }

    private void givenAvailableModel() {
        given(libraryProduct.hasAvailableModel()).willReturn(true);
        given(elibFacade.getLibraryProduct(any(ContentProviderConsumer.class), any(String.class))).willReturn(libraryProduct);
    }

    private void thenActualFormatIdEqualsExpectedFormatId() {
        assertThat(FORMAT_ID, is(actualFormat.getId()));
    }

    private void thenDescriptionEqualsFormatId() {
        assertThat(FORMAT_ID, is(actualFormat.getDescription()));
    }

    private void thenNameEqualsFormatId() {
        assertThat(FORMAT_ID, is(actualFormat.getName()));
    }

    private void whenExecute() {
        final Formats actualFormats = underTest.execute(commandData);
        final Set<Format> formats = actualFormats.getFormats();
        assertEquals(1, formats.size());
        actualFormat = formats.iterator().next();
    }

    private void givenAvailableProduct() {
        given(bookAvailability.isProductAvailable(any(String.class))).willReturn(true);
        given(elibFacade.getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(String.class))).willReturn(bookAvailability);
    }
}
