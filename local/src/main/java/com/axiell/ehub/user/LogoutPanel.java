/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.axiell.ehub.EhubAdminApplication;

/**
 * A {@link Panel} that provides the possibility to logout from the eHUB Administration interface. If the end-user
 * decides to logout she is sent to the home page of the {@link EhubAdminApplication}.
 */
public final class LogoutPanel extends Panel {
    private static final long serialVersionUID = -4761711573336380963L;

    /**
     * Constructs a new {@link LogoutPanel}.
     * 
     * @param id the ID of this {@link Panel}
     */
    public LogoutPanel(final String id) {
        super(id);

        final Link<Void> logoutLink = new Link<Void>("logout") {
            private static final long serialVersionUID = 8215228269723379869L;

            /**
             * @see org.apache.wicket.markup.html.link.Link#onClick()
             */
            @Override
            public void onClick() {
                getSession().invalidate();
                setResponsePage(getApplication().getHomePage());
                getRequestCycle().setRedirect(true);
            }
        };
        add(logoutLink);
    }

}
