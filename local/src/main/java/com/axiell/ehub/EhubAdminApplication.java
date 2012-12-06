/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
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
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {        
        super.init();
        addComponentInstantiationListener(new SpringComponentInjector(this));
        InjectorHolder.getInjector().inject(this);
    }
    
    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.Request, org.apache.wicket.Response)
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new EhubAdminSession(request);
    }
}
