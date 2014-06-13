package com.axiell.ehub.provider.publit;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.cache.CacheFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

abstract class PublitTradeApiFactory {

    public static IPublitTradeApi getApi(final String userName, final String password, final String endpointUrl) {
        Credentials credentials = new UsernamePasswordCredentials(userName, password);
        DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        httpClient.getCredentialsProvider().setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
        final IPublitTradeApi iPublitTradeApi = ProxyFactory.create(IPublitTradeApi.class, endpointUrl, new ApacheHttpClient4Executor(httpClient));
        CacheFactory.makeCacheable(iPublitTradeApi);
        return iPublitTradeApi;
    }
}
