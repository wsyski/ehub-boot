package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.local.provider.ep.EpDataAccessorTestFixture;
import com.axiell.ehub.local.provider.ep.FormatMetadataDTOBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LpfEpDataAccessorTest extends EpDataAccessorTestFixture<LpfCheckoutDTO, LpfEpDataAccessor> {

    @Mock
    protected LpfEpFacade epFacade;

    @Mock
    private LpfCheckoutDTO checkout;

    @BeforeEach
    public void setEpFacade() {
        ReflectionTestUtils.setField(underTest, "epFacade", epFacade);
    }

    @Test
    public void createLoan() {
        givenPatronInCommandData();
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
        whenGetContent();
        thenActualContentLinkContainsHref();
    }


    public void givenCheckout() {
        given(epFacade.checkout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class), any(String.class))).willReturn(checkout);
    }

    public void givenGetCheckout() {
        given(epFacade.getCheckout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class))).willReturn(checkout);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
        given(checkout.getFormatMetadata()).willReturn(FormatMetadataDTOBuilder.defaultFormatMetadataDTO());
    }

    @Override
    protected LpfEpDataAccessor createEpDataAccessor() {
        return new LpfEpDataAccessor();
    }

    @Override
    protected LpfEpFacade getEpFacade() {
        return epFacade;
    }

    @Override
    protected String getContentProviderName() {
        return CONTENT_PROVIDER_TEST_EP;
    }
}
