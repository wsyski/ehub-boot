package com.axiell.ehub.logging;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceFilterBindingFeature implements DynamicFeature {

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext featureContext) {
        final String resourcePackage = resourceInfo.getResourceClass().getPackage().getName();

        if (resourcePackage.startsWith("com.axiell.ehub")) {
            featureContext.register(TimeLoggingClientFilter.class);
            featureContext.register(LoggingFilter.class);
        }
    }
}