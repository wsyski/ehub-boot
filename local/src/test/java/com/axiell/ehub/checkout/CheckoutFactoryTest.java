package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.EhubLoan;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static com.axiell.ehub.checkout.CheckoutMetadataBuilder.checkoutMetadataWithDownloadableFormat;
import static com.axiell.ehub.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static com.axiell.ehub.checkout.ContentLinkBuilder.defaultContentLink;
import static com.axiell.ehub.checkout.ContentBuilder.defaultContent;
import static com.axiell.ehub.checkout.SupplementLinkBuilder.defaultSupplementLink;
import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutFactoryTest {
    private static final String LANGUAGE = "en";
    @Mock
    private EhubLoan ehubLoan;
    private ContentLink contentLink = defaultContentLink();
    private SupplementLink supplementLink = defaultSupplementLink();
    @Mock
    private ICheckoutMetadataFactory checkoutMetadataFactory;
    private CheckoutMetadata checkoutMetadata = checkoutMetadataWithDownloadableFormat();
    private CheckoutFactory underTest;
    private Checkout actualCheckout;

    @Before
    public void setUpUnderTest() {
        underTest = new CheckoutFactory();
        ReflectionTestUtils.setField(underTest, "checkoutMetadataFactory", checkoutMetadataFactory);
    }

    @Test
    public void createFromEhubLoan() throws Exception {
        given(checkoutMetadataFactory.create(any(EhubLoan.class), anyString())).willReturn(checkoutMetadata);
        whenCreate();
        thenActualContentLinkEqualsExpected();
        thenActualCheckoutMetadataEqualsExpected();
    }

    private void whenCreate() {
        actualCheckout = underTest.create(ehubLoan, defaultContent(), LANGUAGE);
    }

    private void thenActualContentLinkEqualsExpected() {
        assertThat(actualCheckout.contentLinks().getContentLinks().size(), Matchers.is(1));
        assertThat(actualCheckout.contentLinks().getContentLinks().get(0), matchesExpectedContentLink(contentLink));
        assertThat(actualCheckout.supplementLinks().getSupplementLinks().size(), Matchers.is(1));
        assertThat(actualCheckout.supplementLinks().getSupplementLinks().get(0), matchesExpectedSupplementLink(supplementLink));
    }

    private void thenActualCheckoutMetadataEqualsExpected() {
        assertThat(actualCheckout.metadata().toDTO(), matchesExpectedCheckoutMetadataDTO(checkoutMetadata.toDTO()));
    }
}
