package com.axiell.ehub.util;

import com.axiell.ehub.security.AuthInfoParamConverterProvider;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.net.URI;

public class RestClientProxyFactoryBean<T> implements FactoryBean<T>, InitializingBean {
    private Class<T> serviceInterface;
    private URI baseUri;
    private T proxy;
    private ClientHttpEngine httpEngine;
    private ResteasyProviderFactory resteasyProviderFactory;
    private AuthInfoParamConverterProvider authInfoParamConverterProvider;

    public T getObject() throws Exception {
        return proxy;
    }

    public Class<T> getObjectType() {
        return serviceInterface;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        if (resteasyProviderFactory == null)
            resteasyProviderFactory = ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(resteasyProviderFactory);

        ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
        if (httpEngine != null) {
            clientBuilder.httpEngine(httpEngine);
        }
        clientBuilder.register(authInfoParamConverterProvider);
        ResteasyClient client = clientBuilder.build();
        ResteasyWebTarget target = client.target(baseUri);
        proxy = target.proxy(serviceInterface);
    }

    public Class<T> getServiceInterface() {
        return serviceInterface;
    }

    @Required
    public void setServiceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    @Required
    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    public ClientHttpEngine getHttpEngine() {
        return httpEngine;
    }

    public void setHttpEngine(ClientHttpEngine httpEngine) {
        this.httpEngine = httpEngine;
    }

    public ResteasyProviderFactory getResteasyProviderFactory() {
        return resteasyProviderFactory;
    }

    public void setResteasyProviderFactory(final ResteasyProviderFactory resteasyProviderFactory) {
        this.resteasyProviderFactory = resteasyProviderFactory;
    }

    @Required
    public void setAuthInfoParamConverterProvider(final AuthInfoParamConverterProvider authInfoParamConverterProvider) {
        this.authInfoParamConverterProvider = authInfoParamConverterProvider;
    }
}
