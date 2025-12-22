package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.ep.EpDataAccessorTestFixture;
import com.axiell.ehub.provider.ep.FormatMetadataDTOBuilder;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(value = MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LppEpDataAccessorTest extends EpDataAccessorTestFixture<LppCheckoutDTO, LppEpDataAccessor> {

    @Mock
    protected LppEpFacade epFacade;

    @Mock
    private LppCheckoutDTO checkout;

    @BeforeEach
    public void setEpFacade() {
        ReflectionTestUtils.setField(underTest, "epFacade", epFacade);
    }

    @Test
    public void createLoan() {
        givenPatronInCommandData();
        givenLanguageInCommandData();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenFormatDecorationInContentProvider();
        givenCompleteCheckout();
        givenCheckout();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    @Test
    public void getContent() {
        givenPatronInCommandData();
        givenLanguageInCommandData();
        givenGetCheckout();
        givenCompleteCheckout();
        givenFormatDecorationInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenContentProviderLoanIdInLoanMetadata();
        givenContentProviderConsumerInCommandData();
        givenContentProviderFormatIdInFormatDecoration();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }


    public void givenCheckout() {
        given(epFacade.checkout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class))).willReturn(checkout);
    }

    public void givenGetCheckout() {
        given(epFacade.getCheckout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class))).willReturn(checkout);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
        given(checkout.getFormatMetadatas()).willReturn(Collections.singletonMap(FormatBuilder.downloadableFormat().getId(),FormatMetadataDTOBuilder.defaultFormatMetadataDTO()));
    }

    @Override
    protected LppEpDataAccessor createEpDataAccessor() {
        return new LppEpDataAccessor();
    }

    @Override
    protected LppEpFacade getEpFacade() {
        return epFacade;
    }

    @Override
    protected String getContentProviderName() {
        return CONTENT_PROVIDER_TEST_EP;
    }
}
