package com.axiell.ehub.support;

import com.axiell.ehub.support.about.AboutPanelFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

class SupportPanel extends BreadCrumbPanel {

    SupportPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addAboutPanelLink(breadCrumbModel);
    }

    private void addAboutPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new AboutPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("aboutLink", breadCrumbModel, factory);
        add(link);
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }
}
