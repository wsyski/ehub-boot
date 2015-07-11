package com.axiell.ehub.provider;

import com.axiell.ehub.provider.alias.IAliasAdminController;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

class ContentProviderNameExistsValidator extends AbstractValidator<String> {
    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    ContentProviderNameExistsValidator() {
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected void onValidate(final IValidatable<String> validatable) {
        final String contentProviderName = validatable.getValue();
        final boolean exists = contentProviderAdminController.existsContentProviderName(contentProviderName);

        if (exists)
            error(validatable, resourceKey());
    }

    @Override
    protected String resourceKey() {
        return "msgContentProviderNameAlreadyExists";
    }
}
