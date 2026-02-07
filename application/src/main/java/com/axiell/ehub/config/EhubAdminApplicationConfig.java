package com.axiell.ehub.config;

import org.apache.wicket.protocol.http.WicketFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EhubAdminApplicationConfig {
    @Bean
    public FilterRegistrationBean<WicketFilter> wicketFilter() {
        FilterRegistrationBean<WicketFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new WicketFilter());
        registration.addInitParameter("configuration", "deployment");
        registration.addInitParameter("applicationClassName", "com.axiell.ehub.EhubAdminApplication");
        registration.addInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/admin/*");
        registration.setName("EhubAdminApplication");
        registration.addUrlPatterns("/admin/*");
        return registration;
    }
}
