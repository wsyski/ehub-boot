package com.axiell.ehub.util;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientFactory.class);
    private RequestConfig requestConfig = RequestConfig.DEFAULT;
    private PoolingHttpClientConnectionManager connectionManager;

    public HttpClientFactory() {
        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException ex) {
            LOGGER.error(ex.getMessage(), ex);
            sslContext = SSLContexts.createDefault();
        }
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                null,
                NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory).build();
        connectionManager = new PoolingHttpClientConnectionManager(registry);
    }

    public CloseableHttpClient createInstance() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public int getConnectionRequestTimeout() {
        return requestConfig.getConnectionRequestTimeout();
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        buildRequestConfig(connectionRequestTimeout, getConnectTimeout(), getSocketTimeout(), getProxy());
    }

    public int getConnectTimeout() {
        return requestConfig.getConnectTimeout();
    }

    public void setConnectTimeout(final int connectTimeout) {
        buildRequestConfig(getConnectionRequestTimeout(), connectTimeout, getSocketTimeout(), getProxy());
    }

    public int getSocketTimeout() {
        return requestConfig.getSocketTimeout();
    }

    public void setSocketTimeout(final int socketTimeout) {
        buildRequestConfig(getConnectionRequestTimeout(), getConnectTimeout(), socketTimeout, getProxy());
    }

    public int getMaxTotal() {
        return connectionManager.getMaxTotal();
    }

    public void setMaxTotal(final int maxTotal) {
        connectionManager.setMaxTotal(maxTotal);
    }

    public int getDefaultMaxPerRoute() {
        return connectionManager.getDefaultMaxPerRoute();
    }

    public void setDefaultMaxPerRoute(final int defaultMaxPerRoute) {
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
    }

    public void setValidateAfterInactivity(final int validateAfterInactivity) {
        connectionManager.setValidateAfterInactivity(validateAfterInactivity);
    }

    public int getValidateAfterInactivity() {
        return connectionManager.getValidateAfterInactivity();
    }

    public PoolStats getTotalStats() {
        return connectionManager.getTotalStats();
    }

    public HttpHost getProxy() {
        return requestConfig.getProxy();
    }

    public void setProxy(HttpHost proxy) {
        buildRequestConfig(getConnectionRequestTimeout(), getConnectTimeout(), getSocketTimeout(), proxy);
    }

    private void buildRequestConfig(final int connectionRequestTimeout, final int connectTimeout, final int socketTimeout, final HttpHost proxy) {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout);
        if (proxy != null) {
            requestConfigBuilder.setProxy(proxy);
        }
        requestConfig = requestConfigBuilder.build();
    }
}
