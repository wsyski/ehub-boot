package com.axiell.ehub.controller;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.IEhubServiceClientFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = EhubApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@DirtiesContext
public class EhubApplication_EhubServiceClientIT {

    @Autowired
    private IEhubServiceClient ehubServiceClient;

    @Test
    public void validAlias() {
        boolean validAlias = ehubServiceClient.isValidAlias("test");
        Assertions.assertFalse(validAlias);
    }

    @Lazy
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public IEhubServiceClient ehubServiceClient(@Value("${local.server.port}") int port, IEhubServiceClientFactory ehubServiceClientFactory) {

            return ehubServiceClientFactory.create("http://localhost:" + port + "/api");
        }
    }
}
