/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * A Panel that displays all available {@link ContentProvider}s in the eHUB.
 */
public final class ContentProvidersPanel extends BreadCrumbPanel {
    private final ContentProvidersListView contentProvidersView;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    public ContentProvidersPanel(String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        this.contentProvidersView = new ContentProvidersListView("contentProviders", breadCrumbModel);
        add(contentProvidersView);
    }

    @Override
    public String getTitle() {
        final StringResourceModel model = new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
        return model.getString();
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final List<ContentProvider> contentProviders = contentProviderAdminController.getContentProviders();
        contentProvidersView.setList(contentProviders);
        super.onActivate(previous);
    }
}
