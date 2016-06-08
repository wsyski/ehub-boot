package com.axiell.ehub.support.request.record;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

public class GetRecordRequestsPanelFactory_v2 implements IBreadCrumbPanelFactory {

    @Override
    public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
        return new GetRecordRequestsPanel_v2(componentId, breadCrumbModel);
    }
}
