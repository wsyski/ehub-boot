package com.axiell.ehub.local.config;

import jakarta.servlet.DispatcherType;
import org.apache.wicket.protocol.http.WicketFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class EhubAdminApplicationConfig {
    @Bean
    public FilterRegistrationBean<WicketFilter> wicketFilter() {
        FilterRegistrationBean<WicketFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        WicketFilter wicketFilter = new WicketFilter();
        filterRegistrationBean.setFilter(wicketFilter);
        filterRegistrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        filterRegistrationBean.addInitParameter("applicationClassName", "com.axiell.ehub.local.EhubAdminApplication");
        filterRegistrationBean.addInitParameter("configuration", "deployment");
        filterRegistrationBean.addInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/admin/*");
        filterRegistrationBean.setName("EhubAdminApplication");
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.addUrlPatterns("/admin/*");
        filterRegistrationBean.setOrder(Integer.MAX_VALUE);
        return filterRegistrationBean;
    }
}
