package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.io.Serializable;
import java.util.Optional;

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

    void afterCancelNewContentProvider(final Optional<AjaxRequestTarget> targetOptional) {
        contentProviderFormContainer.setVisible(false);
        contentProviderCreateLink.setVisible(true);

        targetOptional.ifPresent(
                target -> {
                    target.add(contentProviderFormContainer);
                    target.add(contentProviderCreateLink);
                });
    }

    void afterClickOnContentProviderCreateLink(final Optional<AjaxRequestTarget> targetOptional) {
        contentProviderFormContainer.setVisible(true);

        targetOptional.ifPresent(
                target -> {
                    target.add(contentProviderFormContainer);
                });
    }

    void afterNewContentProvider(final ContentProvider contentProvider) {
        final IBreadCrumbPanelFactory factory = new ContentProviderPanelFactory(contentProvider);
        contentProvidersPanel.activate(factory);
    }
}
