package com.axiell.ehub.controller;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.config.RestApiClientConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, RestApiClientConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@DirtiesContext
public class EhubApplication_EhubClientIT {

    @Autowired
    private IEhubService ehubClient;

    @Test
    public void validAlias() {
        boolean validAlias = ehubClient.isValidAlias("test");
        Assertions.assertFalse(validAlias);
    }
/*
    @Lazy
    @TestConfiguration
    public static class CxfJaxrsClientAddressConfig {

        @Bean(name = "cxfJaxrsClientAddress")
        public String cxfJaxrsClientAddress(@Value("${local.server.port}") int port) {
            return "http://localhost:" + port + "/api";
        }
    }
 */
}
