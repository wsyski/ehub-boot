package com.axiell.ehub.util;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpClientFactory {
    private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    private RequestConfig requestConfig = RequestConfig.DEFAULT;
    private ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(StandardCharsets.UTF_8).build();
    private SocketConfig socketConfig = SocketConfig.DEFAULT;

    public CloseableHttpClient createInstance() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        return HttpClientBuilder.create()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(connectionManager)
                .setDefaultConnectionConfig(connectionConfig)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultSocketConfig(socketConfig)
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
