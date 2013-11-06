package com.axiell.ehub.provider.overdrive;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links.ContentLink;

public class OverDriveCirculationIT extends AbstractOverDriveIT {
    private Checkout checkout;
    private Checkouts checkouts;
    private DownloadLinkTemplate downloadLinkTemplate;
    private DownloadLink downloadLink;

    @After
    public void returnTitle() {
	underTest.returnTitle(contentProviderConsumer, accessToken, PRODUCT_ID);
    }

    @Test
    public void checkout() {
	givenPatronAccessToken();
	givenPatronApiBaseUrl();
	givenErrorPageUrl();
	givenReadAuthUrl();
	whenCheckout();
	thenCheckoutIsNotNull();
	thenCheckoutHasDownloadLinkTemplate();
	thenCheckoutHasExpirationDate();
    }

    private void whenCheckout() {
	checkout = underTest.checkout(contentProviderConsumer, accessToken, PRODUCT_ID, FORMAT_TYPE);
    }

    private void thenCheckoutIsNotNull() {
	Assert.assertNotNull(checkout);
    }

    private void thenCheckoutHasDownloadLinkTemplate() {
	DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(PRODUCT_ID, FORMAT_TYPE);
	downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckout(checkout);
	Assert.assertNotNull(downloadLinkTemplate);
    }

    private void thenCheckoutHasExpirationDate() {
	final Date expirationDate = checkout.getExpirationDate();
	Assert.assertNotNull(expirationDate);
    }

    @Test
    public void getCheckouts() {
	givenPatronAccessToken();
	givenPatronApiBaseUrl();
	givenErrorPageUrl();
	givenReadAuthUrl();
	givenCheckout();
	whenGetCheckouts();
	thenCheckoutListContainsCheckout();
	thenCheckoutHasDownloadLinkTemplate();
    }
    
    private void givenCheckout() {
	checkout = underTest.checkout(contentProviderConsumer, accessToken, PRODUCT_ID, FORMAT_TYPE);
    }

    private void whenGetCheckouts() {
	checkouts = underTest.getCheckouts(contentProviderConsumer, accessToken);
    }

    private void thenCheckoutListContainsCheckout() {
	Assert.assertNotNull(checkouts);
	final List<Checkout> checkoutList = checkouts.getCheckouts();
	Assert.assertTrue(checkoutList.size() == 1);
	checkout = checkoutList.iterator().next();
    }

    @Test
    public void getDownloadLink() {
	givenPatronAccessToken();
	givenPatronApiBaseUrl();
	givenErrorPageUrl();
	givenReadAuthUrl();
	givenCheckout();
	thenCheckoutHasDownloadLinkTemplate();
	whenGetDownloadLink();
	thenDownloadLinkHasContentLinkHref();
    }

    private void whenGetDownloadLink() {
	downloadLink = underTest.getDownloadLink(contentProviderConsumer, accessToken, downloadLinkTemplate);
    }

    private void thenDownloadLinkHasContentLinkHref() {
	Assert.assertNotNull(downloadLink);
	final Links links = downloadLink.getLinks();
	Assert.assertNotNull(links);
	final ContentLink contentLink = links.getContentLink();
	Assert.assertNotNull(contentLink);
	final String contentLinkHref = contentLink.getHref();
	Assert.assertNotNull(contentLinkHref);
    }
}
