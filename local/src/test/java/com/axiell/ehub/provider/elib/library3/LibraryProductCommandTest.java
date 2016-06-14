package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.axiell.ehub.ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LibraryProductCommandTest extends AbstractElib3CommandTest {
    private LibraryProductCommand underTest;
    @Mock
    private LibraryProduct libraryProduct;

    @Before
    public void setUpUnderTest() {
        underTest = new LibraryProductCommand(elibFacade, exceptionFactory);
    }

    @Test
    public void availableModel() {
        givenBasicCommandData();
        givenOnModelAvailableCommand();
        givenAvailableModelInLibraryProduct();
        givenLibraryProduct();
        whenRun();
        thenCommandIsInvoked();
    }

    @Test
    public void noAvailableModel() {
        givenBasicCommandData();
        givenNoAvailableModelInLibraryProduct();
        givenLibraryProduct();
        givenInternalErrorServerExceptionWithProductUnavailable();
        try {
            whenRun();
            thenInternalServerErrorExceptionShouldHaveBeenThrown();
        } catch (InternalServerErrorException e) {
            thenInternalErrorExceptionIsCreatedWithProductUnavailable();
        }
    }

    private void givenNoAvailableModelInLibraryProduct() {
        given(libraryProduct.hasAvailableModel()).willReturn(false);
    }

    private void givenAvailableModelInLibraryProduct() {
        given(libraryProduct.hasAvailableModel()).willReturn(true);
    }

    private void givenLibraryProduct() {
        given(elibFacade.getLibraryProduct(any(ContentProviderConsumer.class), any(String.class))).willReturn(libraryProduct);
    }

    private void whenRun() {
        underTest.run(data);
    }

    private void givenOnModelAvailableCommand() {
        underTest.on(LibraryProductCommand.Result.MODEL_AVAILABLE, next);
    }

    private void givenInternalErrorServerExceptionWithProductUnavailable() {
        given(exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE, language)).willReturn(internalServerErrorException);
    }

    private void thenInternalErrorExceptionIsCreatedWithProductUnavailable() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE, language);
    }
}
