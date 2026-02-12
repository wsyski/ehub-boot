package com.axiell.ehub.local.error;

import com.axiell.ehub.common.ErrorCauseArgumentValue;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

class ErrorCauseArgumentValueTextsSaveButton extends Button {
    private final IModel<ErrorCauseArgumentValue> formModel;
    private final ErrorCauseArgumentValueMediator mediator;

    @SpringBean(name = "errorCauseArgumentValueAdminController")
    private IErrorCauseArgumentValueAdminController errorCauseArgumentValueAdminController;

    ErrorCauseArgumentValueTextsSaveButton(final String id, final IModel<ErrorCauseArgumentValue> formModel, final ErrorCauseArgumentValueMediator mediator) {
        super(id);
        this.formModel = formModel;
        this.mediator = mediator;
    }

    @Override
    public void onSubmit() {
        saveTexts();
        mediator.afterSavedTexts();
    }

    private void saveTexts() {
        final ErrorCauseArgumentValue argumentValue = formModel.getObject();
        errorCauseArgumentValueAdminController.save(argumentValue);
    }
}
