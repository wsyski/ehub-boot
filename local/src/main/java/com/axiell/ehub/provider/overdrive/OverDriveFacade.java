package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO.DownloadLinkTemplateDTO;
import com.axiell.ehub.util.EhubAddress;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_LIBRARY_ID;

@Component
class OverDriveFacade implements IOverDriveFacade {
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String DEFAULT_PIN = "1111";

    @Override
    public OAuthAccessToken getOAuthAccessToken(final ContentProviderConsumer contentProviderConsumer) {
        final AccessTokenResourceFactory accessTokenResourceFactory = new AccessTokenResourceFactory();
        final IAccessTokenResource accessTokenResource = accessTokenResourceFactory.create(contentProviderConsumer);
        final OAuthAuthorizationHeader authorizationHeader = OAuthAuthorizationHeader.fromContentProviderConsumer(contentProviderConsumer);
        return accessTokenResource.getAccessToken(authorizationHeader, GRANT_TYPE_CLIENT_CREDENTIALS);
    }

    @Override
    public Product getProduct(final ContentProviderConsumer contentProviderConsumer, final String productId) {
        final IDiscoveryResource discoveryResource = DiscoveryResourceFactory.create(contentProviderConsumer);
        final IAvailabilityResource availabilityResource = AvailabilityResourceFactory.create(contentProviderConsumer);
        final OAuthAccessToken accessToken = getOAuthAccessToken(contentProviderConsumer);
        final String collectionToken = getCollectionToken(contentProviderConsumer, discoveryResource, accessToken);
        ProductDTO productDTO;
        try {
            productDTO = discoveryResource.getProductById(accessToken, collectionToken, productId);
        }
        catch(Exception ex) {
            productDTO = discoveryResource.getProductByCrossRefId(accessToken, collectionToken, productId);
        }
        final AvailabilityDTO availabilityDTO = availabilityResource.getAvailability(accessToken, collectionToken, productId);
        return new Product(productDTO, availabilityDTO);
    }

    private String getCollectionToken(final ContentProviderConsumer contentProviderConsumer, final IDiscoveryResource discoveryResource,
                                      final OAuthAccessToken accessToken) {
        final String hostAddress = EhubAddress.getProductionHostAddress();
        final String libraryId = contentProviderConsumer.getProperty(OVERDRIVE_LIBRARY_ID);
        final LibraryAccount libraryAccount = discoveryResource.getLibraryAccount(accessToken, hostAddress, libraryId);
        return libraryAccount.getCollectionToken();
    }

    @Override
    public OAuthAccessToken getPatronOAuthAccessToken(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin) {
        final PatronAccessTokenResourceFactory accessTokenResourceFactory = new PatronAccessTokenResourceFactory();
        final IAccessTokenResource accessTokenResource = accessTokenResourceFactory.create(contentProviderConsumer);
        final OAuthAuthorizationHeader authorizationHeader = OAuthAuthorizationHeader.fromContentProviderConsumer(contentProviderConsumer);
        final Scope scope = Scope.fromContentProviderConsumer(contentProviderConsumer);
        final boolean isPasswordRequired = StringUtils.isNotBlank(pin);
        return accessTokenResource.getPatronAccessToken(authorizationHeader, GRANT_TYPE_PASSWORD, libraryCard, isPasswordRequired ? pin : DEFAULT_PIN, isPasswordRequired, scope);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken, final String productId,
                                final String formatType) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        final Fields fields = new Fields.Builder(productId).formatType(formatType).build();
        return circulationResource.checkout(patronAccessToken, fields);
    }

    @Override
    public CheckoutsDTO getCheckouts(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        return circulationResource.getCheckouts(patronAccessToken);
    }

    public CirculationFormatsDTO getCirculationFormats(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken,
                                                       String productId) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        return circulationResource.getCirculationFormats(patronAccessToken, productId);
    }

    @Override
    public CirculationFormatDTO lockFormat(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken,
                                           final String productId, final String formatType) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        final Fields fields = new Fields.Builder(productId).formatType(formatType).build();
        return circulationResource.lockFormat(patronAccessToken, productId, fields);
    }

    @Override
    public DownloadLinkDTO getDownloadLink(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken,
                                           final DownloadLinkTemplateDTO downloadLinkTemplate) {
        final String downloadLinkHref = DownloadLinkHrefFactory.create(contentProviderConsumer, downloadLinkTemplate);
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(downloadLinkHref);
        final IDownloadLinkResource downloadLinkResource = target.proxy(IDownloadLinkResource.class);
        return downloadLinkResource.getDownloadLink(patronAccessToken);
    }

    @Override
    public void checkin(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken, final String productId) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        circulationResource.checkin(patronAccessToken, productId);
    }
}
