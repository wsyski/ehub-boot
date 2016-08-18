package com.axiell.ehub.util;

import com.axiell.ehub.RootResource;
import com.axiell.ehub.checkout.CheckoutsResource;
import com.axiell.ehub.provider.ContentProvidersResource;
import com.axiell.ehub.provider.record.RecordsResource;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JaxRsApplication extends ResourceConfig {
    private static final Logger LOGGER = Logger.getLogger("wire.server");

    public JaxRsApplication() {
        property(ServerProperties.TRACING, "ALL");
        register(new LoggingFeature(LOGGER, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
        register(RequestContextFilter.class);
        register(RootResource.class);
        registerClasses(com.axiell.ehub.security.EhubParamConverterProvider.class);
    }
}
