package com.axiell.ehub.lms.arena.client;

import com.axiell.authinfo.AuthInfoParamConverterProvider;
import com.axiell.ehub.lms.arena.resources.IRootResource;
import org.apache.http.client.HttpClient;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Locale;

public class RootResourceFactory implements IRootResourceFactory {
    private static final String PATH = "";
    private HttpClient httpClient;
    private JacksonProvider jacksonProvider;
    private AuthInfoParamConverterProvider authInfoParamConverterProvider;

    @Override
    public IRootResource createRootResource(final String localRestApiEndpoint, final Locale locale) {
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new ApacheConnectorProvider());
        config.property(org.glassfish.jersey.apache.connector.ApacheClientProperties.CONNECTION_MANAGER,
                httpClient.getConnectionManager());

        Client client = ClientBuilder.newClient(config)
                .register(jacksonProvider)
                .register(authInfoParamConverterProvider)
                .register(new LocalRestApiClientRequestFilter(locale));

        WebTarget target = client.target(localRestApiEndpoint).path(PATH);
        return WebResourceFactory.newResource(IRootResource.class, target);
    }

    @Required
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Required
    public void setAuthInfoParamConverterProvider(final AuthInfoParamConverterProvider authInfoParamConverterProvider) {
        this.authInfoParamConverterProvider = authInfoParamConverterProvider;
    }

    @Required
    public void setJacksonProvider(final JacksonProvider jacksonProvider) {
        this.jacksonProvider = jacksonProvider;
    }
}
