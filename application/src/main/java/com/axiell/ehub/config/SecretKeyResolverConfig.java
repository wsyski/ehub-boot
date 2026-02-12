package com.axiell.ehub.config;

import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.security.EhubAuthHeaderSecretKeyResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretKeyResolverConfig {
    @Value("${ehub.authinfo.expirationTimeInSeconds}")
    private int expirationTimeInSeconds;
    @Value("${ehub.authinfo.leewayInSeconds}")
    private int leewayInSeconds;
    @Value("${ehub.authinfo.debug}")
    private boolean debug;
    @Value("${ehub.authinfo.validate}")
    private boolean validate;

    @Bean
    public IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver(
            IConsumerBusinessController consumerBusinessController

    ) {
        EhubAuthHeaderSecretKeyResolver resolver = new EhubAuthHeaderSecretKeyResolver();
        resolver.setConsumerBusinessController(consumerBusinessController);
        resolver.setValidate(validate);
        resolver.setDebug(debug);
        resolver.setExpirationTimeInSeconds(expirationTimeInSeconds);
        resolver.setLeewayInSeconds(leewayInSeconds);
        return resolver;
    }
}
