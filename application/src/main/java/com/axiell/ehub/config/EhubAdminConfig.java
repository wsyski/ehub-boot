package com.axiell.ehub.config;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import org.apache.wicket.protocol.http.WebApplication;

@ApplicationInitExtension
public class EhubAdminConfig implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
    }
}
