package com.axiell.ehub.provider.record.format;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

import com.axiell.ehub.provider.ContentProviderMediator;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public final class FormatDecorationPanelFactory implements IBreadCrumbPanelFactory {
    private final FormatDecoration formatDecoration;
    private final ContentProviderMediator contentProviderMediator;

    public FormatDecorationPanelFactory(final FormatDecoration formatDecoration,final ContentProviderMediator contentProviderMediator) {
        this.formatDecoration = formatDecoration;
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
        return new FormatDecorationPanel(id, model, formatDecoration, contentProviderMediator);
    }
}