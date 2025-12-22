package com.axiell.ehub.config;

import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.lms.arena.client.ArenaLocalRootResourceFactory;
import com.axiell.ehub.lms.arena.client.IArenaLocalRootResourceFactory;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.axiell.ehub")
public class ArenaLocalClientConfig {


    @Bean(name = "arenaLocalRootResourceFactory")
    public IArenaLocalRootResourceFactory arenaLocalRootResourceFactory(
            final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final JsonProvider jsonProvider,
            final LoggingFeature loggingFeature
    ) {
        return new ArenaLocalRootResourceFactory(authInfoParamConverterProvider, jsonProvider, loggingFeature);
    }


}
