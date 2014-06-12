package com.axiell.ehub.provider.routing;

import org.apache.wicket.validation.validator.StringValidator;

import static com.axiell.ehub.provider.routing.Source.LENGTH;

class SourceLengthValidator extends StringValidator.MaximumLengthValidator {

    SourceLengthValidator() {
        super(LENGTH);
    }

    @Override
    protected String resourceKey() {
        return "msgSourceMaxLength";
    }
}
