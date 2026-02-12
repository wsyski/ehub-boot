package com.axiell.ehub.config;

import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.lms.arena.client.ArenaLocalRootResourceFactory;
import com.axiell.ehub.lms.arena.client.IArenaLocalRootResourceFactory;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArenaLocalClientConfig {

    @Bean
    public IArenaLocalRootResourceFactory arenaLocalRootResourceFactory(
            final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final JacksonJsonProvider jacksonJsonProvider,
            final LoggingFeature loggingFeature
    ) {
        return new ArenaLocalRootResourceFactory(authInfoParamConverterProvider, jacksonJsonProvider, loggingFeature);
    }


}
