package com.axiell.ehub.provider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.io.Serializable;

final class ContentProvidersMediator implements Serializable {
    private ContentProvidersPanel contentProvidersPanel;
    private ContentProviderCreateLink contentProviderCreateLink;
    private WebMarkupContainer contentProviderFormContainer;

    void registerContentProvidersPanel(final ContentProvidersPanel contentProvidersPanel) {
        this.contentProvidersPanel = contentProvidersPanel;
    }

    void registerContentProviderCreateLink(final ContentProviderCreateLink contentProviderCreateLink) {
        this.contentProviderCreateLink = contentProviderCreateLink;
    }

    void registerContentProviderFormContainer(final WebMarkupContainer contentProviderFormContainer) {
        this.contentProviderFormContainer = contentProviderFormContainer;
    }

    void afterDeleteContentProvider() {
        contentProvidersPanel.activate(contentProvidersPanel);
    }

    void afterCancelNewContentProvider(final AjaxRequestTarget target) {
        contentProviderFormContainer.setVisible(false);
        contentProviderCreateLink.setVisible(true);

        if (target != null) {
            target.addComponent(contentProviderFormContainer);
            target.addComponent(contentProviderCreateLink);
        }
    }

    void afterClickOnContentProviderCreateLink(final AjaxRequestTarget target) {
        contentProviderFormContainer.setVisible(true);

        if (target != null) {
            target.addComponent(contentProviderFormContainer);
        }
    }

    void afterNewContentProvider(final ContentProvider contentProvider) {
        final IBreadCrumbPanelFactory factory = new ContentProviderPanelFactory(contentProvider);
        contentProvidersPanel.activate(factory);
    }
}
