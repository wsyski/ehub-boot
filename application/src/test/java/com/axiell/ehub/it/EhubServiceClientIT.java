package com.axiell.ehub.it;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.it.config.EhubServiceClientConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = {EhubApplication.class, EhubServiceClientConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EhubServiceClientIT {

    @Autowired
    private IEhubServiceClient ehubServiceClient;

    @Test
    public void isValidAlias() {
        boolean validAlias = ehubServiceClient.isValidAlias("test");
        Assertions.assertFalse(validAlias);
    }
}
