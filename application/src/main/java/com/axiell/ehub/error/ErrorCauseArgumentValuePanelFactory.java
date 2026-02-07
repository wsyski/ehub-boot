package com.axiell.ehub.error;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

import com.axiell.ehub.ErrorCauseArgumentType;

class ErrorCauseArgumentValuePanelFactory implements IBreadCrumbPanelFactory {
    private final ErrorCauseArgumentType type;

    ErrorCauseArgumentValuePanelFactory(ErrorCauseArgumentType type) {
        this.type = type;
    }

    @Override
    public BreadCrumbPanel create(final String componentId, final IBreadCrumbModel breadCrumbModel) {
        return new ErrorCauseArgumentValuePanel(componentId, breadCrumbModel, type);
    }
}
