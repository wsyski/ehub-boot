package com.axiell.ehub.config;

import com.axiell.ehub.controller.provider.json.JsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfig {

    @Bean(name = "jsonProvider")
    public JsonProvider jsonProvider() {
        return new JsonProvider();
    }
}
