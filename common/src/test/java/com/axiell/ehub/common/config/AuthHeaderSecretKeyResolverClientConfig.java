package com.axiell.ehub.common.config;

import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.common.test.TestSecretKeyConstants;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AuthHeaderSecretKeyResolverClientConfig {

    @Bean
    public IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolverClient() {
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
