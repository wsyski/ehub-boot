package com.axiell.ehub.local.error;


import com.axiell.ehub.local.AbstractBreadCrumbBarPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

public class ErrorCausesBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<ErrorCausesPanel> {

    public ErrorCausesBreadCrumbBarPanel(final String panelId) {
        super(panelId);
    }

    @Override
    public ErrorCausesPanel getActivePanel(final String activePanelId, final IBreadCrumbModel breadCrumbModel) {
        return new ErrorCausesPanel(activePanelId, breadCrumbModel);
    }
}
