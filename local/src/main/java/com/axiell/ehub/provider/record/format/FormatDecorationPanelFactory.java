package com.axiell.ehub.provider.record.format;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

public final class FormatDecorationPanelFactory implements IBreadCrumbPanelFactory {
    private final FormatDecoration formatDecoration;

    public FormatDecorationPanelFactory(final FormatDecoration formatDecoration) {
	this.formatDecoration = formatDecoration;
    }

    @Override
    public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
	return new FormatDecorationPanel(id, model, formatDecoration);
    }
}