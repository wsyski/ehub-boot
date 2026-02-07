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
        addFeedbackPanel();

        final AdminUser user = new AdminUser();
        final StatelessForm<AdminUser> loginForm = makeLoginForm(user);

        addNameField(loginForm);
        addPasswordField(loginForm);
        addLoginButton(user, loginForm);

        add(loginForm);
    }

    private void addFeedbackPanel() {
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
    }

    private StatelessForm<AdminUser> makeLoginForm(final AdminUser user) {
        final CompoundPropertyModel<AdminUser> formModel = new CompoundPropertyModel<>(user);
        return new StatelessForm<>("loginForm", formModel);
    }

    private void addNameField(StatelessForm<AdminUser> loginForm) {
        final RequiredTextField<String> nameField = new RequiredTextField<>("name");
        nameField.setOutputMarkupId(true);
        loginForm.add(nameField);
    }

    private void addPasswordField(StatelessForm<AdminUser> loginForm) {
        final PasswordTextField passwordField = new PasswordTextField("clearPassword");
        passwordField.setOutputMarkupId(true);
        loginForm.add(passwordField);
    }

    private void addLoginButton(final AdminUser user, StatelessForm<AdminUser> loginForm) {
        final LoginButton loginButton = new LoginButton("submit", user);
        loginForm.add(loginButton);
    }
}
