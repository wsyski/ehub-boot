package com.axiell.ehub.provider.alias;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

public class AliasesPanelFactory implements IBreadCrumbPanelFactory  {

    @Override
    public BreadCrumbPanel create(final String componentId, final IBreadCrumbModel breadCrumbModel) {
        return new AliasesPanel(componentId, breadCrumbModel);
    }
}
