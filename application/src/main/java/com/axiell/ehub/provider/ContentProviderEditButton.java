package com.axiell.ehub.provider;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class ContentProviderEditButton extends Button {
    private final IModel<ContentProvider> formModel;
    private final ContentProviderMediator contentProviderMediator;
    
    @SpringBean(name = "contentProviderAdminController") 
    private IContentProviderAdminController contentProviderAdminController;

    ContentProviderEditButton(final String id, final IModel<ContentProvider> formModel, final ContentProviderMediator contentProviderMediator) {
        super(id);
        this.formModel = formModel;
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public void onSubmit() {
        ContentProvider contentProvider = formModel.getObject();
        contentProviderAdminController.save(contentProvider);
        contentProviderMediator.afterEditContentProvider();
    }
}