package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.InternalServerErrorException;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.local.provider.CommandData;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Locale;

import static com.axiell.ehub.common.ErrorCauseArgumentType.PRODUCT_INACTIVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetProductCommandTest extends AbstractElib3CommandTest {
    private static final String FORMAT_ID = "FORMAT_ID";
    private static final String CONTENT_PROVIDER_RECORD_ID = "contentProviderRecordId";
    private static final String LANGUAGE = Locale.ENGLISH.getLanguage();

    private GetProductCommand underTest;
    @Mock
    private Product product;
    @Mock
    private Product.AvailableFormat availableFormat;
    @Mock
    private CommandData commandData;
    private Elib3CommandData elib3CommandData;

    @BeforeEach
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
        givenLanguage();
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
        given(commandData.getContentProviderConsumer()).willReturn(contentProviderConsumer);
        given(commandData.getLanguage()).willReturn(LANGUAGE);
        given(commandData.getContentProviderRecordId()).willReturn(CONTENT_PROVIDER_RECORD_ID);
        elib3CommandData = Elib3CommandData.newInstance(commandData);
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
        underTest.run(elib3CommandData);
    }
}
