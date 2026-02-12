package com.axiell.ehub.local.it;

import com.axiell.ehub.client.IEhubServiceClient;
import com.axiell.ehub.common.config.AuthHeaderSecretKeyResolverClientConfig;
import com.axiell.ehub.local.EhubApplication;
import com.axiell.ehub.local.it.config.EhubServiceClientConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, EhubServiceClientConfig.class, AuthHeaderSecretKeyResolverClientConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class EhubServiceClientIT {

    @Autowired
    private IEhubServiceClient ehubServiceClient;

    @Test
    public void isValidAlias() {
        boolean validAlias = ehubServiceClient.isValidAlias("nonExistingAlias");
        Assertions.assertFalse(validAlias);
    }
}
