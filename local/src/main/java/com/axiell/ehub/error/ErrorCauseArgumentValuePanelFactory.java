package com.axiell.ehub.error;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type;

class ErrorCauseArgumentValuePanelFactory implements IBreadCrumbPanelFactory {
    private final Type type;

    ErrorCauseArgumentValuePanelFactory(Type type) {
        this.type = type;
    }

    @Override
    public BreadCrumbPanel create(final String componentId, final IBreadCrumbModel breadCrumbModel) {
        return new ErrorCauseArgumentValuePanel(componentId, breadCrumbModel, type);
    }
}
