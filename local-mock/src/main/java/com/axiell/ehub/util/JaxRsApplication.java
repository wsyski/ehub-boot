package com.axiell.ehub.util;

import com.axiell.ehub.security.AuthInfoParamConverterProvider;
import com.axiell.ehub.RootResource;
import com.axiell.ehub.security.AuthorizationHeaderInterceptor;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JaxRsApplication extends ResourceConfig {
    private static final Logger LOGGER = Logger.getLogger("wire.server");

    public JaxRsApplication() {
        register(new LoggingFeature(LOGGER, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
        register(RequestContextFilter.class);
        register(RootResource.class);
        registerClasses(AuthInfoParamConverterProvider.class, AuthorizationHeaderInterceptor.class, EhubRuntimeExceptionMapper.class, RuntimeExceptionMapper.class);
    }
}
