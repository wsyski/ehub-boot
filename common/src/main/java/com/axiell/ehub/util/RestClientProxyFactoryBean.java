package com.axiell.ehub.util;

import com.axiell.authinfo.AuthInfoParamConverterProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URI;

public class RestClientProxyFactoryBean<T> implements FactoryBean<T>, InitializingBean {
    private Class<T> serviceInterface;
    private URI baseUri;
    private T proxy;
    private CloseableHttpClient httpClient;
    private AuthInfoParamConverterProvider authInfoParamConverterProvider;

    @Override
    public T getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class<T> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new ApacheConnectorProvider());

        if (httpClient != null) {
            config.property(ApacheClientProperties.CONNECTION_MANAGER_SHARED, true);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        JacksonJaxbJsonProvider jacksonProvider = new JacksonJaxbJsonProvider();
        jacksonProvider.setMapper(objectMapper);

        Client client = ClientBuilder.newClient(config)
                .register(jacksonProvider)
                .register(authInfoParamConverterProvider);

        WebTarget target = client.target(baseUri);
        proxy = WebResourceFactory.newResource(serviceInterface, target);
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

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Required
    public void setAuthInfoParamConverterProvider(final AuthInfoParamConverterProvider authInfoParamConverterProvider) {
        this.authInfoParamConverterProvider = authInfoParamConverterProvider;
    }
}