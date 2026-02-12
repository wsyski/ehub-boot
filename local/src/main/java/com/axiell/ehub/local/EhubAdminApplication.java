/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Represents the eHUB administration application.
 */
public final class EhubAdminApplication extends WebApplication {

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return IndexPage.class;
    }

    /**
     * @see WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();
        getDebugSettings().setDevelopmentUtilitiesEnabled(true);
        new BeanValidationConfiguration().configure(this);
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        Injector.get().inject(this);
    }

    /**
     * @see WebApplication#newSession(Request, Response)
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new EhubAdminSession(request);
    }
}
