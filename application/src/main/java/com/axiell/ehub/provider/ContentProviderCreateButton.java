package com.axiell.ehub.provider;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class ContentProviderCreateButton extends Button {
    private final ContentProvidersMediator contentProvidersMediator;
    private final IModel<ContentProvider> formModel;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    ContentProviderCreateButton(final String id, final ContentProvidersMediator contentProvidersMediator, final IModel<ContentProvider> formModel) {
        super(id);
        this.contentProvidersMediator = contentProvidersMediator;
        this.formModel = formModel;
    }

    @Override
    public void onSubmit() {
        ContentProvider contentProvider = formModel.getObject();
        contentProvider = contentProviderAdminController.save(contentProvider);
        contentProvidersMediator.afterNewContentProvider(contentProvider);
    }
}