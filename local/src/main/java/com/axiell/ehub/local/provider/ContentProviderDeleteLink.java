package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.local.ConfirmationLink;
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
