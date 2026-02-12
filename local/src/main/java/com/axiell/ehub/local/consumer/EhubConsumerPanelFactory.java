package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

final class EhubConsumerPanelFactory implements IBreadCrumbPanelFactory {
    private final EhubConsumer ehubConsumer;

    EhubConsumerPanelFactory(EhubConsumer ehubConsumer) {
        this.ehubConsumer = ehubConsumer;
    }

    @Override
    public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
        return new EhubConsumerPanel(id, model, ehubConsumer);
    }
}
