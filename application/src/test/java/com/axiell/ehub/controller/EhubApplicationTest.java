package com.axiell.ehub.controller;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.config.RestApiClientConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = {EhubApplication.class, RestApiClientConfig.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
public class EhubApplicationTest {

    @Autowired
    private IEhubService ehubClient;

    @Test
    public void validAlias() {
        boolean validAlias = ehubClient.isValidAlias("test");
        Assertions.assertFalse(validAlias);
    }
}
