package com.axiell.ehub.security;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class SslHttpClientBuilder {
    private static final String SCHEME_NAME = "https";
    private static final String SSL_PROTOCOL = "SSL";

    /**
     * Private constructor that prevents direct instantiation.
     */
    private SslHttpClientBuilder() {
    }

    public static DefaultHttpClient createSelfSignedCertSSLCapableHttpConnector(int port) {
        java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        final ClientConnectionManager connectionManager = makeClientConnectionManager(port);
        final HttpParams params = new BasicHttpParams();
        return new DefaultHttpClient(connectionManager, params);
    }

    private static ClientConnectionManager makeClientConnectionManager(int port) {
        final SSLSocketFactory socketFactory = makeSocketFactory();
        final Scheme httpsScheme = new Scheme(SCHEME_NAME, port, socketFactory);
        final ClientConnectionManager connectionManager = new ThreadSafeClientConnManager();
        connectionManager.getSchemeRegistry().register(httpsScheme);
        return connectionManager;
    }

    private static SSLSocketFactory makeSocketFactory() {
        final X509TrustManager trustManager = new UnsafeX509TrustManager();
        final SSLContext sslContext = initSslContext(trustManager);
        return new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    private static SSLContext initSslContext(final X509TrustManager trustManager) {
        // Now put the trust manager into an SSLContext.
        // Supported: SSL, SSLv2, SSLv3, TLS, TLSv1, TLSv1.1
        final TrustManager[] trustManagers = new TrustManager[]{trustManager};
        final SecureRandom secureRandom = new SecureRandom();
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            sslContext.init(null, trustManagers, secureRandom);
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            throw new SslHttpClientException("Error in initializing SSL capable http client!!", ex);
        }
        return sslContext;
    }
}
