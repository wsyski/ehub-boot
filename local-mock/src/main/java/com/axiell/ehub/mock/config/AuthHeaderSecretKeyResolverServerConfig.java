package com.axiell.ehub.mock.config;

import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AuthHeaderSecretKeyResolverServerConfig {
    @Value("${ehub.authinfo.secretKey}")
    private String secretKey;
    @Value("${ehub.authinfo.expirationTimeInSeconds}")
    private int expirationTimeInSeconds;
    @Value("${ehub.authinfo.leewayInSeconds}")
    private int leewayInSeconds;
    @Value("${ehub.authinfo.debug}")
    private boolean debug;
    @Value("${ehub.authinfo.validate}")
    private boolean validate;

    @Bean
    public IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolverServer(

    ) {
        ConstantAuthHeaderSecretKeyResolver resolver = new ConstantAuthHeaderSecretKeyResolver(
                secretKey,
                expirationTimeInSeconds,
                leewayInSeconds
        );
        resolver.setValidate(validate);
        resolver.setDebug(debug);
        return resolver;
    }
}
