/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public final class LogoutPanel extends Panel {

    public LogoutPanel(final String id) {
        super(id);
        addLogoutLink();
    }

    private void addLogoutLink() {
	final Link<Void> logoutLink = new LogoutLink("logout");
        add(logoutLink);
    }

}
