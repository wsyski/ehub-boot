package com.axiell.ehub.config;

import com.axiell.authinfo.ConstantAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretKeyResolverConfig {
    public static final String SECRET_KEY = "c2VjcmV0S2V5MTM5NDA=";
    public static final long TOKEN_EXPIRATION_TIME_IN_SECONDS = 0L;
    public static final long TOKEN_LEEWAY_IN_SECONDS = 3600L;
    public static final boolean IS_TOKEN_DEBUG = true;
    public static final boolean IS_TOKEN_VALIDATE = true;

    @Bean
    public IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver() {
        ConstantAuthHeaderSecretKeyResolver resolver = new ConstantAuthHeaderSecretKeyResolver(
                SECRET_KEY,
                TOKEN_EXPIRATION_TIME_IN_SECONDS,
                TOKEN_LEEWAY_IN_SECONDS
        );
        resolver.setValidate(IS_TOKEN_VALIDATE);
        resolver.setDebug(IS_TOKEN_DEBUG);
        return resolver;
    }
}

