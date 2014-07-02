package com.axiell.ehub.provider.alias;

import org.apache.wicket.validation.validator.StringValidator;

import static com.axiell.ehub.provider.alias.Alias.LENGTH;

class AliasLengthValidator extends StringValidator.MaximumLengthValidator {

    AliasLengthValidator() {
        super(LENGTH);
    }

    @Override
    protected String resourceKey() {
        return "msgAliasMaxLength";
    }
}
