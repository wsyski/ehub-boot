package com.axiell.ehub.config;

import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.security.EhubAuthHeaderSecretKeyResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ehub.properties")
public class SecretKeyResolverConfig {
    @Value("${auth.expirationTimeInSeconds}")
    private int expirationTimeInSeconds;
    @Value("${auth.leewayInSeconds}")
    private int leewayInSeconds;
    @Value("${auth.debug}")
    private boolean debug;
    @Value("${auth.validate}")
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
