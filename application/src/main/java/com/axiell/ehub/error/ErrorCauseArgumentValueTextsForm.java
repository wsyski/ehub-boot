package com.axiell.ehub.error;


import com.axiell.ehub.ErrorCauseArgumentValue;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

class ErrorCauseArgumentValueTextsForm extends StatelessForm<ErrorCauseArgumentValue> {

    ErrorCauseArgumentValueTextsForm(final String id, final ErrorCauseArgumentValue errorCauseArgumentValue, final ErrorCauseArgumentValueMediator mediator) {
        super(id);

        final IModel<ErrorCauseArgumentValue> formModel = new Model<>(errorCauseArgumentValue);
        setModel(formModel);

        addTextsListView(formModel);
        addSaveButton(mediator, formModel);
    }

    private void addTextsListView(final IModel<ErrorCauseArgumentValue> formModel) {
        final ErrorCauseArgumentValueTextsListView textsListView = new ErrorCauseArgumentValueTextsListView("texts", formModel);
        add(textsListView);
    }

    private void addSaveButton(final ErrorCauseArgumentValueMediator mediator, final IModel<ErrorCauseArgumentValue> formModel) {
        final ErrorCauseArgumentValueTextsSaveButton saveButton = new ErrorCauseArgumentValueTextsSaveButton("save", formModel, mediator);
        add(saveButton);
    }
}
