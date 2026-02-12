package com.axiell.ehub.mock.it;

import com.axiell.ehub.mock.EhubMockApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EhubMockApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class EhubMockApplicationIT {

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        // Basic context load testdata
    }

    @Test
    void actuatorHealth() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + port + "/actuator/health";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("UP");
    }

}
