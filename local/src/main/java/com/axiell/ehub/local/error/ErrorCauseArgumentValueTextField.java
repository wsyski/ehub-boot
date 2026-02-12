package com.axiell.ehub.local.error;

import com.axiell.ehub.common.ErrorCauseArgumentValueTextBundle;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;


class ErrorCauseArgumentValueTextField extends TextField<String> {

    ErrorCauseArgumentValueTextField(final String id, final IModel<String> model) {
        super(id, model);
        addLengthValidator();
    }

    private void addLengthValidator() {
        final StringValidator lengthValidator = new StringValidator(1, ErrorCauseArgumentValueTextBundle.MAX_TEXT_LENGTH);
        add(lengthValidator);
    }
}
