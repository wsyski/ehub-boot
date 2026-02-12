/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local;

import com.axiell.ehub.local.user.LoginPanel;

/**
 * The home page of the {@link EhubAdminApplication}.
 */
public class IndexPage extends AbstractBasePage {

    /**
     * Constructs a new {@link IndexPage}.
     */
    public IndexPage() {
        final LoginPanel loginPanel = new LoginPanel("login");
        add(loginPanel);
    }
}
