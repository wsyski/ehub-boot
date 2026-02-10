package com.axiell.ehub.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@DirtiesContext
public class EhubApplication_HealthIT {

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        // Basic context load test
    }

    @Test
    void actuatorHealth() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + port + "/actuator/health";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("UP");
    }

}
