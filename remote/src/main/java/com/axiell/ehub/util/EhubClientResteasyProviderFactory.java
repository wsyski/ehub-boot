package com.axiell.ehub.util;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

public abstract class EhubClientResteasyProviderFactory extends ResteasyProviderFactory {

    public static ResteasyProviderFactory getInstance() {
        final ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        providerFactory.addClientErrorInterceptor(new EhubExceptionUnmarshallerClientInterceptor());
        return providerFactory;
    }

}
