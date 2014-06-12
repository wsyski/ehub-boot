/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.provider.routing.RoutingRulesPanelFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * A Panel that displays all available {@link ContentProvider}s in the eHUB.
 */
final class ContentProvidersPanel extends BreadCrumbPanel {
    private final ContentProvidersListView contentProvidersView;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    ContentProvidersPanel(String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        this.contentProvidersView = new ContentProvidersListView("contentProviders", breadCrumbModel);
        add(contentProvidersView);
        addRoutingRulesLink(breadCrumbModel);
    }

    private void addRoutingRulesLink(final IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new RoutingRulesPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("routingRulesLink", breadCrumbModel, factory);
        add(link);
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
