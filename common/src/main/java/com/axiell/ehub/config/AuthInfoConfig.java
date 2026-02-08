package com.axiell.ehub.config;

import com.axiell.authinfo.IAuthHeaderParser;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.jwt.JwtAuthHeaderParser;
import com.axiell.ehub.controller.provider.converter.AuthInfoConverter;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.axiell.ehub")
public class AuthInfoConfig {

    @Bean
    public JwtAuthHeaderParser jwtAuthHeaderParser(final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        return new JwtAuthHeaderParser(authHeaderSecretKeyResolver);
    }

    @Bean
    public AuthInfoConverter authInfoConverter(final JwtAuthHeaderParser jwtAuthHeaderParser) {
        Map<String, IAuthHeaderParser> authHeaderParsers = new HashMap<>();
        authHeaderParsers.put(JwtAuthHeaderParser.BEARER_SCHEME, jwtAuthHeaderParser);
        return new AuthInfoConverter(authHeaderParsers, IAuthHeaderParser.BEARER_SCHEME);
    }

    @Bean
    public AuthInfoParamConverterProvider authInfoParamConverterProvider(final AuthInfoConverter authInfoConverter) {
        return new AuthInfoParamConverterProvider(authInfoConverter);
    }
}
