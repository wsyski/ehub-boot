package com.axiell.ehub.error;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

class ErrorCauseArgumentValueTextField extends TextField<String> {

    ErrorCauseArgumentValueTextField(final String id, final IModel<String> model) {
        super(id, model);
        addLengthValidator();
    }

    private void addLengthValidator() {
        final ErrorCauseArgumentValueTextLengthValidator lengthValidator = new ErrorCauseArgumentValueTextLengthValidator();
        add(lengthValidator);
    }
}
