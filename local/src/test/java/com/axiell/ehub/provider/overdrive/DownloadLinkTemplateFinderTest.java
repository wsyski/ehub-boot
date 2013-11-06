package com.axiell.ehub.provider.overdrive;

import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DownloadLinkTemplateFinderTest {
    private static final String PRODUCT_ID = "productId";
    private static final String FORMAT_ID = "formatId";
    private DownloadLinkTemplateFinder underTest;
    @Mock
    private Checkout checkout;
    @Mock
    private CirculationFormat circulationFormat;
    @Mock
    private LinkTemplates linkTemplates;
    @Mock
    private Checkouts checkouts;
    @Mock
    private DownloadLinkTemplate downloadLinkTemplate;
    private DownloadLinkTemplate actualDownloadLinkTemplate;

    @Before
    public void setUp() {
	underTest = new DownloadLinkTemplateFinder(PRODUCT_ID, FORMAT_ID);
    }

    @Test
    public void findFromCheckout() {
	givenCirculationFormats();
	givenReserveId();
	givenFormatType();
	givenLinkTemplates();
	givenDownloadLinkTemplate();
	whenFindFromCheckout();
	thenDownloadLinkTemplateIsNotNull();
    }

    private void givenCirculationFormats() {
	final List<CirculationFormat> circulationFormats = Arrays.asList(circulationFormat);
	given(checkout.getFormats()).willReturn(circulationFormats);
    }

    private void givenFormatType() {
	given(circulationFormat.getFormatType()).willReturn(FORMAT_ID);
    }

    private void givenReserveId() {
	given(circulationFormat.getReserveId()).willReturn(PRODUCT_ID);
    }

    private void givenLinkTemplates() {
	given(circulationFormat.getLinkTemplates()).willReturn(linkTemplates);
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
    
    @Test
    public void findFromCheckoutWhenNotSameFormat() {
	givenCheckoutList();
	givenCirculationFormats();
	whenFindFromCheckout();
	thenDownloadLinkTemplateIsNull();
    }

    private void thenDownloadLinkTemplateIsNull() {
	Assert.assertNull(actualDownloadLinkTemplate);
    }

    @Test
    public void findFromCheckouts() {
	givenCheckoutList();
	givenCirculationFormats();
	givenReserveId();
	givenFormatType();
	givenLinkTemplates();
	givenDownloadLinkTemplate();
	whenFindFromCheckouts();
	thenDownloadLinkTemplateIsNotNull();
    }

    private void whenFindFromCheckouts() {
	actualDownloadLinkTemplate = underTest.findFromCheckouts(checkouts);
    }

    private void givenCheckoutList() {
	final List<Checkout> checkoutList = Arrays.asList(checkout);
	given(checkouts.getCheckouts()).willReturn(checkoutList);
    }
    
    @Test
    public void findFromCheckoutsWhenNotSameFormat() {
	givenCheckoutList();
	givenCirculationFormats();
	try {
	    whenFindFromCheckouts();
	    Assert.fail("A NotFoundException should not have been thrown");
	} catch (NotFoundException e) {
	    EhubAssert.thenNotFoundExceptionIsThrown(e);
	}
		
    }
}
