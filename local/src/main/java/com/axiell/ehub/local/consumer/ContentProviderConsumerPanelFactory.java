package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

class ContentProviderConsumerPanelFactory implements IBreadCrumbPanelFactory {
    private final ContentProviderConsumer contentProviderConsumer;

    ContentProviderConsumerPanelFactory(final ContentProviderConsumer contentProviderConsumer) {
        this.contentProviderConsumer = contentProviderConsumer;
    }

    @Override
    public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
        return new ContentProviderConsumerPanel(id, model, contentProviderConsumer);
    }
}
