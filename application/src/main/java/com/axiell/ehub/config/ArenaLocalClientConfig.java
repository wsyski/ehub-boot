package com.axiell.ehub.config;

import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.lms.arena.client.ArenaLocalRootResourceFactory;
import com.axiell.ehub.lms.arena.client.IArenaLocalRootResourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ehub.properties")
@ComponentScan(basePackages = "com.axiell.ehub")
public class ArenaLocalClientConfig {
    @Value("${client.verbose}")
    private boolean verbose;
    @Value("${client.prettyLogging}")
    private boolean prettyLogging;

    @Bean(name = "arenaLocalRootResourceFactory")
    public IArenaLocalRootResourceFactory arenaLocalRootResourceFactory(
            final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final JsonProvider jsonProvider
    ) {
        return new ArenaLocalRootResourceFactory(jsonProvider, authInfoParamConverterProvider, verbose, prettyLogging);
    }


}
