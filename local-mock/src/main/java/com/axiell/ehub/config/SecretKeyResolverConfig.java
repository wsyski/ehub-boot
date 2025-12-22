package com.axiell.ehub.config;

import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ehub.properties")
public class SecretKeyResolverConfig {
    @Value("${auth.secretKey}")
    private String secretKey;
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
