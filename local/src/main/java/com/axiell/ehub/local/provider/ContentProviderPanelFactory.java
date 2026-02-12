package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

final class ContentProviderPanelFactory implements IBreadCrumbPanelFactory {
    private final ContentProvider contentProvider;

    ContentProviderPanelFactory(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    @Override
    public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
        final String contentProviderName = contentProvider.getName();
        return new ContentProviderPanel(id, model, contentProviderName);
    }
}
