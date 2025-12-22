package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import static com.axiell.ehub.checkout.CheckoutMetadataBuilder.checkoutMetadataWithDownloadableFormat;
import static com.axiell.ehub.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static com.axiell.ehub.checkout.ContentBuilder.contentWithSupplementLinks;
import static com.axiell.ehub.checkout.ContentLinkBuilder.defaultContentLink;
import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkBuilder.defaultSupplementLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CheckoutFactoryTest {
    private static final String LANGUAGE = "en";
    @Mock
    private EhubLoan ehubLoan;
    private ContentLink contentLink = defaultContentLink();
    private SupplementLink supplementLink = defaultSupplementLink();
    @Mock
    private ICheckoutMetadataFactory checkoutMetadataFactory;
    @Mock
    private FormatDecoration formatDecoration;

    private CheckoutMetadata checkoutMetadata = checkoutMetadataWithDownloadableFormat();
    private CheckoutFactory underTest;
    private Checkout actualCheckout;

    @BeforeEach
    public void setUpUnderTest() {
        underTest = new CheckoutFactory();
        ReflectionTestUtils.setField(underTest, "checkoutMetadataFactory", checkoutMetadataFactory);
    }

    @Test
    public void createFromEhubLoan() throws Exception {
        given(checkoutMetadataFactory.create(any(EhubLoan.class), any(FormatDecoration.class), anyString(), anyBoolean())).willReturn(checkoutMetadata);
        whenCreate();
        thenActualContentLinkEqualsExpected();
        thenActualCheckoutMetadataEqualsExpected();
    }

    private void whenCreate() {
        actualCheckout = underTest.create(ehubLoan, formatDecoration, contentWithSupplementLinks(), LANGUAGE, false);
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
