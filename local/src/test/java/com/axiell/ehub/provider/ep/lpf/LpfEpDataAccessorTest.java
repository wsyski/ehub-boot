package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.EpDataAccessorTestFixture;
import com.axiell.ehub.provider.ep.FormatMetadataDTOBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LpfEpDataAccessorTest extends EpDataAccessorTestFixture<LpfCheckoutDTO, LpfEpDataAccessor> {

    @Mock
    protected LpfEpFacade epFacade;

    @Mock
    private LpfCheckoutDTO checkout;

    @Before
    public void setEpFacade() {
        ReflectionTestUtils.setField(underTest, "epFacade", epFacade);
    }

    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenFormatDecorationFromContentProvider();
        givenCompleteCheckout();
        givenCheckout();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    @Test
    public void getContent() {
        givenLanguageInCommandData();
        givenGetCheckout();
        givenCompleteCheckout();
        givenFormatDecorationInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenContentProviderLoanIdFromLoanMetadata();
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
