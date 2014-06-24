package com.axiell.ehub.error;

import org.apache.wicket.validation.validator.StringValidator;

import static com.axiell.ehub.ErrorCauseArgumentValueTextBundle.MAX_TEXT_LENGTH;

class ErrorCauseArgumentValueTextLengthValidator extends StringValidator.MaximumLengthValidator {

    ErrorCauseArgumentValueTextLengthValidator() {
        super(MAX_TEXT_LENGTH);
    }

    @Override
    protected String resourceKey() {
        return "msgMaxTextLength";
    }
}
