package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ep.EpDataAccessorTestFixture;
import com.axiell.ehub.provider.ep.FormatMetadataDTOBuilder;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LppEpDataAccessorTest extends EpDataAccessorTestFixture<LppCheckoutDTO, LppEpDataAccessor> {

    @Mock
    protected LppEpFacade epFacade;

    @Mock
    private LppCheckoutDTO checkout;

    @Before
    public void setEpFacade() {
        ReflectionTestUtils.setField(underTest, "epFacade", epFacade);
    }

    @Test
    public void createLoan() {
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
