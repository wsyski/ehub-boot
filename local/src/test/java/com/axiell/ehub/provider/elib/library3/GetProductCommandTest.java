package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.PRODUCT_INACTIVE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class GetProductCommandTest extends AbstractElib3CommandTest {
    private static final String FORMAT_ID = "FORMAT_ID";
    private GetProductCommand underTest;
    @Mock
    private Product product;
    @Mock
    private Product.AvailableFormat availableFormat;
    private Elib3CommandData commandData;

    @Before
    public void setUpUnderTest() {
        underTest = new GetProductCommand(elibFacade, exceptionFactory);
    }

    @Test
    public void isActive() {
        givenCommandData();
        givenAvailableFormats();
        givenActiveProduct();
        givenProductFromElib();
        givenNextCommand();
        whenRun();
        thenCommandIsInvoked();
    }

    @Test
    public void isInactive() {
        givenCommandData();
        givenInactiveProduct();
        givenProductFromElib();
        givenInternalServerErrorExceptionWithProductInactive();
        try {
            whenRun();
            thenInternalServerErrorExceptionShouldHaveBeenThrown();
        } catch (InternalServerErrorException e) {
            thenInternalErrorExceptionIsCreatedWithProductInactive();
        }
    }

    private void givenInternalServerErrorExceptionWithProductInactive() {
        given(exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_INACTIVE, language)).willReturn(internalServerErrorException);
    }

    private void thenInternalErrorExceptionIsCreatedWithProductInactive() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_INACTIVE, language);
    }

    private void givenInactiveProduct() {
        given(product.isActive()).willReturn(false);
    }

    private void givenCommandData() {
        commandData = Elib3CommandData.newInstance(contentProviderConsumer, libraryCard, elibProductId, language);
    }

    private void givenAvailableFormats() {
        given(availableFormat.getId()).willReturn(FORMAT_ID);
        List<Product.AvailableFormat> availableFormats = Lists.newArrayList(availableFormat);
        given(product.getFormats()).willReturn(availableFormats);
    }

    private void givenActiveProduct() {
        given(product.isActive()).willReturn(true);
    }

    private void givenProductFromElib() {
        given(elibFacade.getProduct(any(ContentProviderConsumer.class), any(String.class))).willReturn(product);
    }

    private void givenNextCommand() {
        underTest.next(next);
    }

    private void whenRun() {
        underTest.run(commandData);
    }
}
