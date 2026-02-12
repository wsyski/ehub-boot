/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local;

import com.axiell.ehub.local.user.AdminUser;
import lombok.Getter;
import lombok.Setter;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * The {@link WebSession} of the {@link EhubAdminApplication}.
 */
@Getter
@Setter
public final class EhubAdminSession extends WebSession {
    private AdminUser adminUser;

    public EhubAdminSession(Request request) {
        super(request);
    }
}
