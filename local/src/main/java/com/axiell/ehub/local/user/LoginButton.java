package com.axiell.ehub.local.user;

import com.axiell.ehub.local.EhubAdminPage;
import com.axiell.ehub.local.EhubAdminSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@Slf4j
public final class LoginButton extends Button {
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
        log.debug("Login status for user '" + user.getName() + "': '" + status + "'");
        final StringResourceModel resourceModel = new StringResourceModel("msgInvalidCredentials", this, new Model<>());
        final String errorMessage = resourceModel.getString();
        error(errorMessage);
    }
}
