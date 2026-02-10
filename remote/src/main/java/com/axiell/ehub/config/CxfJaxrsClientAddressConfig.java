package com.axiell.ehub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfJaxrsClientAddressConfig {

    @Bean(name = "cxfJaxrsClientAddress")
    public String cxfJaxrsClientAddress(@Value("${cxf.jaxrs.client.address}") String address) {
        return address;
    }
}
