package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.model.IModel;

class ContentProviderEditForm extends AbstractContentProviderForm {

    ContentProviderEditForm(final String id, final ContentProviderMediator contentProviderMediator) {
        super(id);
        addEditButton(contentProviderMediator, formModel);
    }

    private void addEditButton(final ContentProviderMediator contentProviderMediator, final IModel<ContentProvider> formModel) {
        final ContentProviderEditButton editButton = new ContentProviderEditButton("submit", formModel, contentProviderMediator);
        add(editButton);
    }
}
