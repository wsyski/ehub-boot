package com.axiell.ehub.lms.arena.client;

import com.axiell.authinfo.AuthInfoParamConverterProvider;
import com.axiell.ehub.lms.arena.resources.IRootResource;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Locale;

public class RootResourceFactory implements IRootResourceFactory {
    private static final String PATH = "";
    private final ResteasyProviderFactory resteasyProviderFactory = ResteasyProviderFactory.getInstance();
    private ClientHttpEngine httpEngine;
    private String baseUrl;
    private JacksonProvider jacksonProvider;
    private AuthInfoParamConverterProvider authInfoParamConverterProvider;

    @Override
    public IRootResource createRootResource(final String localRestApiEndpoint, final Locale locale) {
        RegisterBuiltin.register(resteasyProviderFactory);
        ResteasyClient client = new ResteasyClientBuilder()
                .register(jacksonProvider)
                .register(authInfoParamConverterProvider)
                .register(new LocalRestApiClientRequestFilter(locale))
                .httpEngine(httpEngine).build();
        ResteasyWebTarget target = client.target(localRestApiEndpoint).path(PATH);
        return target.proxy(IRootResource.class);
    }

    @Required
    public void setHttpEngine(final ClientHttpEngine httpEngine) {
        this.httpEngine = httpEngine;
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
