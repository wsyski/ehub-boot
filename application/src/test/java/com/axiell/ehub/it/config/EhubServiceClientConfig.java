package com.axiell.ehub.it.config;

import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.IEhubServiceClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@Lazy
@TestConfiguration
public class EhubServiceClientConfig {
    @Bean
    public IEhubServiceClient ehubServiceClient(@Value("${local.server.port}") int port, IEhubServiceClientFactory ehubServiceClientFactory) {

        return ehubServiceClientFactory.create("http://localhost:" + port + "/api");
    }
}
