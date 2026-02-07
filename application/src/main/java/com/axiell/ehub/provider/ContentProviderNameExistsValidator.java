package com.axiell.ehub.provider;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class ContentProviderNameExistsValidator implements IValidator<String> {
    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    public ContentProviderNameExistsValidator() {
        Injector.get().inject(this);
    }

    @Override
    public void validate(final IValidatable<String> validatable) {
        final String contentProviderName = validatable.getValue();
        final boolean exists = contentProviderAdminController.existsContentProviderName(contentProviderName);

        if (exists)
            validatable.error(new ValidationError().addKey(resourceKey()));
    }

    private String resourceKey() {
        return "msgContentProviderNameAlreadyExists";
    }
}
