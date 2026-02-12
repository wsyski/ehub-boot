package com.axiell.ehub.local.config;

import com.axiell.ehub.common.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.local.lms.arena.client.ArenaLocalRootResourceFactory;
import com.axiell.ehub.local.lms.arena.client.IArenaLocalRootResourceFactory;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArenaLocalClientConfig {

    @Bean
    public IArenaLocalRootResourceFactory arenaLocalRootResourceFactory(
            @Qualifier("authInfoParamConverterProviderServer") final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final JacksonJsonProvider jacksonJsonProvider,
            final LoggingFeature loggingFeature
    ) {
        return new ArenaLocalRootResourceFactory(authInfoParamConverterProvider, jacksonJsonProvider, loggingFeature);
    }
}
