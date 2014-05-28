package com.axiell.ehub.logging;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

public abstract class TimeLoggingResteasyProviderFactory extends ResteasyProviderFactory {

    public static ResteasyProviderFactory getInstance() {
        final ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        providerFactory.registerProvider(TimeLoggingExecutionInterceptor.class);
        return providerFactory;
    }

}
