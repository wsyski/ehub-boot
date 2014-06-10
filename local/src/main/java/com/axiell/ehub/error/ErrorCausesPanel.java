package com.axiell.ehub.error;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

class ErrorCausesPanel extends BreadCrumbPanel {

    ErrorCausesPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addErrorCauseArgumentValuesListView(breadCrumbModel);
    }

    private void addErrorCauseArgumentValuesListView(final IBreadCrumbModel breadCrumbModel) {
        final ErrorCauseArgumentValuesListView errorCauseArgumentValuesListView = new ErrorCauseArgumentValuesListView("errorCauseArgumentValues", breadCrumbModel);
        add(errorCauseArgumentValuesListView);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
