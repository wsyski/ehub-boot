/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.user.LoginPanel;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

@WicketHomePage
public class IndexPage extends AbstractBasePage {

    /**
     * Constructs a new {@link IndexPage}.
     */
    public IndexPage() {
        final LoginPanel loginPanel = new LoginPanel("login");
        add(loginPanel);
    }
}
