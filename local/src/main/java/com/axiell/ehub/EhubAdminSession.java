/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.apache.wicket.request.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.axiell.ehub.user.AdminUser;

/**
 * The {@link WebSession} of the {@link EhubAdminApplication}.
 */
public final class EhubAdminSession extends WebSession {
    private static final long serialVersionUID = -1066711627671962365L;
    private AdminUser adminUser;

    /**
     * Constructs a new {@link EhubAdminSession}.
     *
     * @param request the request
     */
    public EhubAdminSession(Request request) {
        super(request);
    }

    /**
     * Returns the {@link AdminUser}.
     *
     * @return the {@link AdminUser}, <code>null</code> the end-user is not logged in
     */
    public AdminUser getAdminUser() {
        return adminUser;
    }

    /**
     * Sets the {@link AdminUser}.
     *
     * @param adminUser the {@link AdminUser} to set
     */
    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }
}
