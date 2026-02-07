package com.axiell.ehub.user;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;

final class LogoutLink extends Link<Void> {

    LogoutLink(String id) {
        super(id);
    }

    @Override
    public void onClick() {
        invalidateSession();
        setResponsePage();
    }

    private void invalidateSession() {
        getSession().invalidate();
    }

    private void setResponsePage() {
        Application application = getApplication();
        final Class<? extends Page> homePage = application.getHomePage();
        setResponsePage(homePage);
    }
}

