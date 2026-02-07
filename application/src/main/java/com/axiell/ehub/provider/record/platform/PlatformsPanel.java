/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

final class PlatformsPanel extends BreadCrumbPanel {
    private final PlatformsListView languagesListView;
    private final PlatformAddFormPanel platformAddFormPanel;

    @SpringBean(name = "platformAdminController")
    private IPlatformAdminController platformAdminController;

    PlatformsPanel(final String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        final PlatformsMediator mediator = new PlatformsMediator();
        mediator.registerPlatformsPanel(this);

        languagesListView = new PlatformsListView("platforms", mediator);
        add(languagesListView);

        platformAddFormPanel = new PlatformAddFormPanel("platformAddFormPanel", new Platform(), mediator);
        add(platformAddFormPanel);
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final List<Platform> languages = platformAdminController.findAll();
        languagesListView.setList(languages);
        platformAddFormPanel.resetForm();
        super.onActivate(previous);
    }
}
