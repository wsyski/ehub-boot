package com.axiell.ehub.config;

import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.test.TestSecretKeyConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretKeyResolverConfig {

    @Bean
    public IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver() {
        ConstantAuthHeaderSecretKeyResolver resolver = new ConstantAuthHeaderSecretKeyResolver(
                TestSecretKeyConstants.SECRET_KEY,
                TestSecretKeyConstants.TOKEN_EXPIRATION_TIME_IN_SECONDS,
                TestSecretKeyConstants.TOKEN_LEEWAY_IN_SECONDS
        );
        resolver.setValidate(TestSecretKeyConstants.IS_TOKEN_VALIDATE);
        resolver.setDebug(TestSecretKeyConstants.IS_TOKEN_DEBUG);
        return resolver;
    }
}
