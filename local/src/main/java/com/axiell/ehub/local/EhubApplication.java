package com.axiell.ehub.local;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.axiell.ehub.common", "com.axiell.ehub.core", "com.axiell.ehub.local"})
@EntityScan(basePackages = {"com.axiell.ehub.common", "com.axiell.ehub.local"})
@EnableJpaRepositories("com.axiell.ehub.local")
@EnableAspectJAutoProxy
public class EhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(EhubApplication.class, args);
    }

}
