package com.axiell.ehub.it;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.it.config.EhubServiceClientConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, EhubServiceClientConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@DirtiesContext
public class EhubServiceClientIT {

    @Autowired
    private IEhubServiceClient ehubServiceClient;

    @Test
    public void validAlias() {
        boolean validAlias = ehubServiceClient.isValidAlias("test");
        Assertions.assertFalse(validAlias);
    }
}
