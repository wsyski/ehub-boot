package com.axiell.ehub.local.provider.alias;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class AliasExistsValidator implements IValidator<String> {
    @SpringBean(name = "aliasAdminController")
    private IAliasAdminController aliasAdminController;

    public AliasExistsValidator() {
        Injector.get().inject(this);
    }

    @Override
    public void validate(final IValidatable<String> validatable) {
        final String aliasValue = validatable.getValue();
        final boolean exists = aliasAdminController.existsAlias(aliasValue);

        if (exists)
            validatable.error(new ValidationError().addKey(resourceKey()));
    }

    protected String resourceKey() {
        return "msgAliasAlreadyExists";
    }
}
