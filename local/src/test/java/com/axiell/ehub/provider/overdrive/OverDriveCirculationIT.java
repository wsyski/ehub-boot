package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.overdrive.DownloadLinkTemplateDTO;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links.ContentLink;
import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;

public class OverDriveCirculationIT extends AbstractOverDriveIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(OverDriveCirculationIT.class);

    private CheckoutDTO checkout;
    private CheckoutsDTO checkouts;
    private DownloadLinkTemplateDTO downloadLinkTemplate;
    private DownloadLinkDTO downloadLink;

    @After
    public void afterTest() {
        givenCheckinAll();
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
        final List<CheckoutDTO> checkoutList = checkouts.getCheckouts();
        Assert.assertTrue(checkoutList.size() > 0);
        IMatcher<CheckoutDTO> matcher=new CheckoutMatcher(PRODUCT_ID);
        checkout = new CollectionFinder<CheckoutDTO>().find(matcher, checkoutList);
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

    private void givenCheckinAll() {
        final CheckoutsDTO checkouts = underTest.getCheckouts(contentProviderConsumer, accessToken);
        for (CheckoutDTO checkout: checkouts.getCheckouts()) {
            try {
                if (!checkout.isFormatLocked()) {
                    underTest.checkin(contentProviderConsumer, accessToken, checkout.getReserveId());
                }
            }
            catch (BadRequestException ex) {
                LOGGER.error("Can not checkin product id: "+checkout.getReserveId());
            }
        }
    }

}
