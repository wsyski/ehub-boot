package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.util.EhubAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
    public Product getProduct(final ContentProviderConsumer contentProviderConsumer, final String crossRefId, final String formatType) {
        final IDiscoveryResource discoveryResource = DiscoveryResourceFactory.create(contentProviderConsumer);
        final IAvailabilityResource availabilityResource = AvailabilityResourceFactory.create(contentProviderConsumer);
        final OAuthAccessToken accessToken = getOAuthAccessToken(contentProviderConsumer);
        final String collectionToken = getCollectionToken(contentProviderConsumer, discoveryResource, accessToken);
        final ProductsDTO productsDTO = discoveryResource.getProductsByCrossRefId(accessToken, collectionToken, crossRefId);
        if (productsDTO == null || productsDTO.getTotalItems() == 0) {
            throw NotFoundExceptionFactory.create(ErrorCause.CONTENT_PROVIDER_RECORD_NOT_FOUND, ContentProvider.CONTENT_PROVIDER_OVERDRIVE, crossRefId,
                    formatType);
        }
        final ProductDTO productDTO = productsDTO.getProducts().get(0);
        final AvailabilityDTO availabilityDTO = availabilityResource.getAvailability(accessToken, collectionToken, productDTO.getId());
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
        try {
            URL url = new URL(downloadLinkHref);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", patronAccessToken.toString());
            connection.setInstanceFollowRedirects(false);
            int status = connection.getResponseCode();
            if (status != Response.Status.FOUND.getStatusCode()) {
                throw new InternalServerErrorException("Invalid status from get fulfillment location");
            }
            String location = connection.getHeaderField("Location");
            connection.disconnect();
            return new DownloadLinkDTO(location);
        } catch (IOException ex) {
            throw new InternalServerErrorException("Could not get fulfillment location", ex);
        }
    }

    @Override
    public void checkin(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken, final String productId) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        circulationResource.checkin(patronAccessToken, productId);
    }
}
