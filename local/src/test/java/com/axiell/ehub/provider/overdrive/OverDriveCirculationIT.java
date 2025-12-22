package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links.ContentLink;
import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;

@Slf4j
public class OverDriveCirculationIT extends AbstractOverDriveIT {

    private CheckoutDTO checkout;
    private CheckoutsDTO checkouts;
    private DownloadLinkTemplateDTO downloadLinkTemplate;
    private DownloadLinkDTO downloadLink;

    @AfterEach
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
        Assertions.assertNotNull(checkout);
    }

    private void thenCheckoutHasDownloadLinkTemplate() {
        DownloadLinkTemplateFinder downloadLinkTemplateFinder = new DownloadLinkTemplateFinder(PRODUCT_ID, FORMAT_TYPE);
        downloadLinkTemplate = downloadLinkTemplateFinder.findFromCheckout(checkout);
        Assertions.assertNotNull(downloadLinkTemplate);
    }

    private void thenCheckoutHasExpirationDate() {
        final Date expirationDate = checkout.getExpirationDate();
        Assertions.assertNotNull(expirationDate);
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
        Assertions.assertNotNull(checkouts);
        final List<CheckoutDTO> checkoutList = checkouts.getCheckouts();
        Assertions.assertTrue(checkoutList.size() > 0);
        IMatcher<CheckoutDTO> matcher = new CheckoutMatcher(PRODUCT_ID);
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
        Assertions.assertNotNull(downloadLink);
        final Links links = downloadLink.getLinks();
        Assertions.assertNotNull(links);
        final ContentLink contentLink = links.getContentLink();
        Assertions.assertNotNull(contentLink);
        final String contentLinkHref = contentLink.getHref();
        Assertions.assertNotNull(contentLinkHref);
    }

    private void givenCheckinAll() {
        final CheckoutsDTO checkouts = underTest.getCheckouts(contentProviderConsumer, accessToken);
        for (CheckoutDTO checkout : checkouts.getCheckouts()) {
            try {
                if (!checkout.isFormatLocked()) {
                    underTest.checkin(contentProviderConsumer, accessToken, checkout.getReserveId());
                }
            } catch (BadRequestException ex) {
                log.error("Can not checkin product id: " + checkout.getReserveId());
            }
        }
    }

}
