package com.axiell.ehub.provider.ep;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.AuthInfoConverter;
import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.Patron;
import com.axiell.authinfo.jwt.JwtAuthHeaderParser;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Collections;

import static com.axiell.authinfo.IAuthHeaderParser.BEARER_SCHEME;

class EpClientRequestFilter implements ClientRequestFilter {

    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;
    private AuthInfoConverter authInfoConverter;


    public EpClientRequestFilter(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
        ConstantAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver = new ConstantAuthHeaderSecretKeyResolver();
        String secretKey = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY);
        long expirationTimeInSeconds = Long.parseLong(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_TOKEN_EXPIRATION_TIME_IN_SECONDS));
        authHeaderSecretKeyResolver.setSecretKey(secretKey);
        authHeaderSecretKeyResolver.setExpirationTimeInSeconds(expirationTimeInSeconds);
        JwtAuthHeaderParser authHeaderParser = new JwtAuthHeaderParser();
        authHeaderParser.setAuthHeaderSecretKeyResolver(authHeaderSecretKeyResolver);
        authInfoConverter = new AuthInfoConverter();
        authInfoConverter.setAuthHeaderParsers(Collections.singletonMap(BEARER_SCHEME, authHeaderParser));
        authInfoConverter.setDefaultScheme(BEARER_SCHEME);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        long ehubConsumerId = ehubConsumer.getId();
        String siteId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID);
        AuthInfo authInfo = new AuthInfo.Builder().siteId(siteId).ehubConsumerId(ehubConsumerId).patron(new Patron.Builder().id(patron.hasId() ? patron.getId() : null).libraryCard(patron.getLibraryCard()).email(patron.getEmail()).build()).build();
        headers.add(HttpHeaders.AUTHORIZATION, authInfoConverter.toString(authInfo));
    }
}
