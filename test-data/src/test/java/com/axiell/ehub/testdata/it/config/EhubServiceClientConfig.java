package com.axiell.ehub.testdata.it.config;

import com.axiell.ehub.client.IEhubServiceClient;
import com.axiell.ehub.client.IEhubServiceClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

@Lazy
@TestConfiguration
@ComponentScan(basePackages = { "com.axiell.ehub.client" })
public class EhubServiceClientConfig {
    @Bean
    public IEhubServiceClient ehubServiceClient(@Value("${local.server.port}") final int port, final IEhubServiceClientFactory ehubServiceClientFactory) {

        return ehubServiceClientFactory.create("http://localhost:" + port + "/api");
    }
}
