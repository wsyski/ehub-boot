package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.local.AbstractBreadCrumbBarPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

public class PlatformsBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<PlatformsPanel> {

    public PlatformsBreadCrumbBarPanel(final String panelId) {
        super(panelId);
    }

    @Override
    public PlatformsPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
        return new PlatformsPanel(activePanelId, breadCrumbModel);
    }
}
