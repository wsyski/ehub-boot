package com.axiell.ehub.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {"com.axiell.ehub.common", "com.axiell.ehub.core", "com.axiell.ehub.mock"})
@EnableAspectJAutoProxy
public class EhubMockApplication {

    public static void main(String[] args) {
        SpringApplication.run(EhubMockApplication.class, args);
    }

}
