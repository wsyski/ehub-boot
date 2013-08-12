package com.axiell.ehub.provider.publit;

import com.axiell.ehub.provider.publit.api.IPublitTradeApi;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.cache.CacheFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.util.HashMap;
import java.util.Map;

import static java.text.MessageFormat.format;

abstract class PublitTradeApiFactory {
    private static final String USER_PASS_ENDPOINT_FORMAT = "{0}:{1}:{2}";
    private static Map<String, IPublitTradeApi> tradeApis = new HashMap<>();

    static IPublitTradeApi getApi(final String userName, final String password, final String endpointUrl) {
        if (!tradeApis.containsKey(format(USER_PASS_ENDPOINT_FORMAT, userName, password, endpointUrl))) {
            addNewTradeApi(userName, password, endpointUrl);
        }
        return tradeApis.get(format(USER_PASS_ENDPOINT_FORMAT, userName, password, endpointUrl));
    }

    private static IPublitTradeApi addNewTradeApi(final String userName, final String password, final String endpointUrl) {
        return tradeApis.put(format(USER_PASS_ENDPOINT_FORMAT, userName, password, endpointUrl), constructNewProxy(userName, password, endpointUrl));
    }

    private static IPublitTradeApi constructNewProxy(final String userName, final String password, final String endpointUrl) {
        Credentials credentials = new UsernamePasswordCredentials(userName, password);
        DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        httpClient.getCredentialsProvider().setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
        final IPublitTradeApi iPublitTradeApi = ProxyFactory.create(IPublitTradeApi.class, endpointUrl, new ApacheHttpClient4Executor(httpClient));
        CacheFactory.makeCacheable(iPublitTradeApi);
        return iPublitTradeApi;
    }


}
