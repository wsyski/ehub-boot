package com.axiell.ehub.error;


import com.axiell.ehub.AbstractBreadCrumbBarPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

public class ErrorCausesBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<ErrorCausesPanel> {

    public ErrorCausesBreadCrumbBarPanel(final String panelId) {
        super(panelId);
    }

    @Override
    public ErrorCausesPanel getActivePanel(final String activePanelId, final IBreadCrumbModel breadCrumbModel) {
        return new ErrorCausesPanel(activePanelId, breadCrumbModel);
    }
}
