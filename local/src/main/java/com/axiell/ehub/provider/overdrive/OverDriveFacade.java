package com.axiell.ehub.provider.overdrive;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_LIBRARY_ID;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springframework.stereotype.Component;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;
import com.axiell.ehub.util.EhubAddress;

@Component
class OverDriveFacade implements IOverDriveFacade {
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    private static final String GRANT_TYPE_PASSWORD = "password";

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
        final OAuthAccessToken accessToken = getOAuthAccessToken(contentProviderConsumer);
        final String collectionToken = getCollectionToken(contentProviderConsumer, discoveryResource, accessToken);
        return discoveryResource.getProduct(accessToken, collectionToken, productId);
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
        return accessTokenResource.getPatronAccessToken(authorizationHeader, GRANT_TYPE_PASSWORD, libraryCard, pin, scope);
    }

    @Override
    public Checkout checkout(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken, final String productId,
                             final String formatType) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        final Fields fields = new Fields.Builder(productId).formatType(formatType).build();
        return circulationResource.checkout(patronAccessToken, fields);
    }

    @Override
    public Checkouts getCheckouts(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        return circulationResource.getCheckouts(patronAccessToken);
    }

    @Override
    public DownloadLink getDownloadLink(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken,
                                        final DownloadLinkTemplate downloadLinkTemplate) {
        final String downloadLinkHref = DownloadLinkHrefFactory.create(contentProviderConsumer, downloadLinkTemplate);
        final IDownloadLinkResource downloadLinkResource = ProxyFactory.create(IDownloadLinkResource.class, downloadLinkHref);
        return downloadLinkResource.getDownloadLink(patronAccessToken);
    }

    @Override
    public void returnTitle(final ContentProviderConsumer contentProviderConsumer, final OAuthAccessToken patronAccessToken, final String productId) {
        final ICirculationResource circulationResource = CirculationResourceFactory.create(contentProviderConsumer);
        circulationResource.returnTitle(patronAccessToken, productId);
    }
}
