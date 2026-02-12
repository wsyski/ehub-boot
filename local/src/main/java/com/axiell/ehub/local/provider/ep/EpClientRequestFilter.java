package com.axiell.ehub.local.provider.ep;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.Patron;
import com.axiell.authinfo.jwt.JwtAuthHeaderParser;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.controller.provider.converter.AuthInfoConverter;
import jakarta.annotation.Priority;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Collections;

import static com.axiell.authinfo.IAuthHeaderParser.BEARER_SCHEME;

@ConstrainedTo(RuntimeType.CLIENT)
@Priority(Integer.MAX_VALUE)
@Provider
public final class EpClientRequestFilter implements ClientRequestFilter {

    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;
    private AuthInfoConverter authInfoConverter;


    public EpClientRequestFilter(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
        String secretKey = contentProviderConsumer
                .getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY);
        long expirationTimeInSeconds = Long.parseLong(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_TOKEN_EXPIRATION_TIME_IN_SECONDS));
        ConstantAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver = new ConstantAuthHeaderSecretKeyResolver(secretKey, expirationTimeInSeconds, 0L);
        JwtAuthHeaderParser authHeaderParser = new JwtAuthHeaderParser(authHeaderSecretKeyResolver);
        authInfoConverter = new AuthInfoConverter(Collections.singletonMap(BEARER_SCHEME, authHeaderParser), BEARER_SCHEME);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        long eHubConsumerId = ehubConsumer.getId();
        String siteId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID);
        Patron patronInfo = new Patron.Builder().id(patron.hasId() ? patron.getId() : null)
                .libraryCard(patron.getLibraryCard())
                .email(patron.getEmail())
                .birthDate(patron.getBirthDate())
                .build();
        AuthInfo authInfo = new AuthInfo.Builder().siteId(siteId).ehubConsumerId(eHubConsumerId).patron(patronInfo).build();
        headers.add(HttpHeaders.AUTHORIZATION, authInfoConverter.toString(authInfo));
    }
}
