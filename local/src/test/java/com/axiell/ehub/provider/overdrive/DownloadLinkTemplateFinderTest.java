package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
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

    @Before
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
        Assert.assertNotNull(actualDownloadLinkTemplate);
    }


}
