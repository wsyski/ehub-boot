package com.axiell.ehub.client.config;

import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.common.config.AbstractAuthInfoParamConverterProviderConfig;
import com.axiell.ehub.common.controller.provider.converter.AuthInfoParamConverterProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthInfoParamConverterProviderClientConfig extends AbstractAuthInfoParamConverterProviderConfig {

    @Bean
    public AuthInfoParamConverterProvider authInfoParamConverterProviderClient(@Qualifier("authHeaderSecretKeyResolverClient") final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        return authInfoParamConverterProvider(authHeaderSecretKeyResolver);
    }

}
