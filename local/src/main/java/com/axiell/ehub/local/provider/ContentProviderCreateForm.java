package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;

class ContentProviderCreateForm extends AbstractContentProviderForm {

    ContentProviderCreateForm(final String id, final ContentProvidersMediator contentProvidersMediator) {
        super(id);
        addNameField(formModel);
        addCreateButton(contentProvidersMediator);
    }

    private void addNameField(final IModel<ContentProvider> formModel) {
        final ContentProviderNameModel nameModel = new ContentProviderNameModel(formModel);
        final RequiredTextField<String> nameField = new RequiredTextField<>("name", nameModel);
        nameField.setOutputMarkupPlaceholderTag(true);
        nameField.add(new ContentProviderNameExistsValidator());
        add(nameField);
    }

    private void addCreateButton(final ContentProvidersMediator contentProvidersMediator) {
        final ContentProviderCreateButton button = new ContentProviderCreateButton("submit", contentProvidersMediator, formModel);
        add(button);
    }
}
