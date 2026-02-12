package com.axiell.ehub.local.error;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

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
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }
}
