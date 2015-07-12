package com.axiell.ehub.provider;

import com.axiell.ehub.ConfirmationLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class ContentProviderDeleteLink extends ConfirmationLink<ContentProvider> {
    private final ContentProvider contentProvider;
    private final ContentProvidersMediator contentProvidersMediator;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    ContentProviderDeleteLink(final String id, final ContentProvider contentProvider, final ContentProvidersMediator contentProvidersMediator) {
        super(id);
        this.contentProvider = contentProvider;
        this.contentProvidersMediator = contentProvidersMediator;
        setVisible(contentProvider.isEhubProvider());
    }

    @Override
    public void onClick() {
        contentProviderAdminController.delete(contentProvider);
        contentProvidersMediator.afterDeleteContentProvider();
    }
}