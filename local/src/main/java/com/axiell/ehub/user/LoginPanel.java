/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.EhubAdminPage;
import com.axiell.ehub.EhubAdminSession;

/**
 * A {@link Panel} that provides the possibility for the end-user to login to the eHUB Administration interface. If the
 * end-user is successfully logged in she is sent to the {@link EhubAdminPage}.
 */
public final class LoginPanel extends Panel {
    private static final long serialVersionUID = -8387035246210184104L;
    private static final Logger LOGGER = Logger.getLogger(LoginPanel.class);

    @SpringBean(name = "userAdminController")
    private IUserAdminController userAdminController;

    /**
     * Constructs a new {@link LoginPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     */
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

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = 8650216690439979415L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                final LoginStatus status = userAdminController.login(user);

                switch (status) {
                    case SUCCESS:
                        final EhubAdminSession session = (EhubAdminSession) getSession();
                        session.setAdminUser(user);
                        final EhubAdminPage ehubAdminPage = new EhubAdminPage(user);
                        setResponsePage(ehubAdminPage);
                        break;
                    default:
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Login status for user '" + user.getName() + "': '" + status + "'");
                        }
                        final StringResourceModel resourceModel = new StringResourceModel("msgInvalidCredentials", LoginPanel.this, new Model<>());
                        final String errorMessage = resourceModel.getString();
                        error(errorMessage);
                        break;
                }
            }
        };
        loginForm.add(submitButton);

        add(loginForm);
    }
}
