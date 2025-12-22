package com.axiell.ehub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:/com/axiell/ehub/rest-api-context.xml"})
public class RestApiServerConfig {

}
