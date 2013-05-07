package com.axiell.ehub;

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
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public final class SslHttpClientBuilder {

    private static final String HTTPS = "https";

    public static DefaultHttpClient createSelfSignedCertSSLCapableHttpConnector(int port) {
        try {
            java.lang.System.setProperty(
                    "sun.security.ssl.allowUnsafeRenegotiation", "true");

            // First create a trust manager that won't care.
            X509TrustManager trustManager = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // Don't do anything.
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // Don't do anything.
                }

                public X509Certificate[] getAcceptedIssuers() {
                    // Don't do anything.
                    return null;
                }
            };

            // Now put the trust manager into an SSLContext.
            // Supported: SSL, SSLv2, SSLv3, TLS, TLSv1, TLSv1.1
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager},
                    new SecureRandom());

            // Use the above SSLContext to create socket factory
            SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            // correct protocol name.
            Scheme httpsScheme = new Scheme(HTTPS, port, sf);

            HttpParams params = new BasicHttpParams();
            ClientConnectionManager connectionManager = new ThreadSafeClientConnManager();
            connectionManager.getSchemeRegistry().register(httpsScheme);

            return new DefaultHttpClient(connectionManager, params);
        } catch (Exception ex) {
            throw new RuntimeException("Error in initializing SSL capable http client!!", ex);

        }
    }
}
