package com.axiell.ehub.support;

import com.axiell.ehub.AbstractBreadCrumbBarPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

public class SupportBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<SupportPanel> {

    public SupportBreadCrumbBarPanel(final String panelId) {
        super(panelId);
    }

    @Override
    public SupportPanel getActivePanel(final String activePanelId, final IBreadCrumbModel breadCrumbModel) {
        return new SupportPanel(activePanelId, breadCrumbModel);
    }
}
