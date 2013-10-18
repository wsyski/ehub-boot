package com.axiell.ehub.user;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.EhubAdminPage;
import com.axiell.ehub.EhubAdminSession;

final class LoginButton extends Button {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginButton.class);
    private final AdminUser user;

    @SpringBean(name = "userAdminController")
    private IUserAdminController userAdminController;

    LoginButton(final String id, final AdminUser user) {
	super(id);
	this.user = user;
    }

    @Override
    public void onSubmit() {
	final LoginStatus status = userAdminController.login(user);

	switch (status) {
	case SUCCESS:
	    handleSuccessfulLogin();
	    break;
	default:
	    handleInvalidLogin(status);
	    break;
	}
    }

    private void handleSuccessfulLogin() {
	final EhubAdminSession session = (EhubAdminSession) getSession();
	session.setAdminUser(user);
	final EhubAdminPage ehubAdminPage = new EhubAdminPage(user);
	setResponsePage(ehubAdminPage);
    }

    private void handleInvalidLogin(final LoginStatus status) {
	if (LOGGER.isDebugEnabled()) {
	LOGGER.debug("Login status for user '" + user.getName() + "': '" + status + "'");
	}
	final StringResourceModel resourceModel = new StringResourceModel("msgInvalidCredentials", this, new Model<>());
	final String errorMessage = resourceModel.getString();
	error(errorMessage);
    }
}