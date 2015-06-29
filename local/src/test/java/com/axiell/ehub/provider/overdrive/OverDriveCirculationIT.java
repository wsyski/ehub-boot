package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.ocd.MediaDTO;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links.ContentLink;
import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class OverDriveCirculationIT extends AbstractOverDriveIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(OverDriveCirculationIT.class);

    private Checkout checkout;
    private Checkouts checkouts;
    private DownloadLinkTemplate downloadLinkTemplate;
    private DownloadLink downloadLink;

    @After
    public void afterTest() {
        final Checkouts checkouts = underTest.getCheckouts(contentProviderConsumer, accessToken);
        for (Checkout checkout: checkouts.getCheckouts()) {
            try {
                underTest.checkin(contentProviderConsumer, accessToken, checkout.getReserveId());
            }
            catch (ClientResponseFailure ex) {
                LOGGER.error("Can not checkin product id: "+checkout.getReserveId());
            }
        }
        //underTest.checkin(contentProviderConsumer, accessToken, PRODUCT_ID);
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
    public void getCheckouts() throws IFinder.NotFoundException {
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

    private void thenCheckoutListContainsCheckout() throws IFinder.NotFoundException {
        Assert.assertNotNull(checkouts);
        final List<Checkout> checkoutList = checkouts.getCheckouts();
        Assert.assertTrue(checkoutList.size() > 0);
        IMatcher<Checkout> matcher=new CheckoutMatcher(PRODUCT_ID);
        checkout = new CollectionFinder<Checkout>().find(matcher, checkoutList);
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
