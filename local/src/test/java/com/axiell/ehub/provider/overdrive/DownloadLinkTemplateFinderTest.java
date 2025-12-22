package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Map;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DownloadLinkTemplateFinderTest {
    private static final String PRODUCT_ID = "productId";
    private static final String FORMAT_ID = "formatId";
    private DownloadLinkTemplateFinder underTest;
    @Mock
    private CheckoutDTO checkout;
    private Map<String, DownloadLinkTemplateDTO> circulationLinks = Collections.singletonMap("downloadRedirect", new DownloadLinkTemplateDTO("href"));
    @Mock
    private LinkTemplatesDTO linkTemplates;
    @Mock
    private CheckoutsDTO checkouts;
    @Mock
    private DownloadLinkTemplateDTO downloadLinkTemplate;
    private DownloadLinkTemplateDTO actualDownloadLinkTemplate;

    @BeforeEach
    public void setUp() {
        underTest = new DownloadLinkTemplateFinder(PRODUCT_ID, FORMAT_ID);
    }

    @Test
    public void findFromCheckout() {
        givenCirculationLinks();
        givenDownloadLinkTemplate();
        whenFindFromCheckout();
        thenDownloadLinkTemplateIsNotNull();
    }

    private void givenCirculationLinks() {
        given(checkout.getLinks()).willReturn(circulationLinks);
    }

    private void givenDownloadLinkTemplate() {
        given(linkTemplates.getDownloadLink()).willReturn(downloadLinkTemplate);
    }

    private void whenFindFromCheckout() {
        actualDownloadLinkTemplate = underTest.findFromCheckout(checkout);
    }

    private void thenDownloadLinkTemplateIsNotNull() {
        Assertions.assertNotNull(actualDownloadLinkTemplate);
    }
}
