/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

public final class LoginPanel extends Panel {

    public LoginPanel(String panelId) {
        super(panelId);

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);

        final AdminUser user = new AdminUser();
        final CompoundPropertyModel<AdminUser> formModel = new CompoundPropertyModel<>(user);
        StatelessForm<AdminUser> loginForm = new StatelessForm<>("loginForm", formModel);

        final RequiredTextField<String> nameField = new RequiredTextField<>("name");
        loginForm.add(nameField);

        final PasswordTextField passwordField = new PasswordTextField("clearPassword");
        loginForm.add(passwordField);

        final LoginButton loginButton = new LoginButton("submit", user);
        loginForm.add(loginButton);

        add(loginForm);
    }
}
