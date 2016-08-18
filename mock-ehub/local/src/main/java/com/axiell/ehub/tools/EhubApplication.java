package com.axiell.ehub.tools;

import com.axiell.ehub.RootResource;
import com.axiell.ehub.checkout.CheckoutsResource;
import com.axiell.ehub.provider.ContentProvidersResource;
import com.axiell.ehub.provider.record.RecordsResource;
import com.axiell.ehub.security.EhubParamConverterProvider;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class EhubApplication extends ResourceConfig {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EhubApplication.class);

    public EhubApplication() {
        // Enable Tracing support.
        //property(ServerProperties.TRACING, "ALL");
        register(new LoggingFilter(Logger.getLogger(EhubApplication.class.getName()), true));
        /*
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(RootResource.class).to(IRootResource.class);
                bind(RecordsResource.class).to(IRecordsResource.class);
                bind(CheckoutsResource.class).to(ICheckoutsResource.class);
                bind(ContentProvidersResource.class).to(IContentProvidersResource.class);
            }
        });
        */
        //register(ICheckoutsResource.class);
        //register(IRootResource.class);
        //register(IRecordsResource.class);
        // register(EhubParamConverterProvider.class);
        register(RootResource.class);
        register(CheckoutsResource.class);
        register(RecordsResource.class);
        register(ContentProvidersResource.class);
//        registerClasses(RootResourceMock.class, CheckoutsResourceMock.class, RecordsResourceMock.class);
    }



}
