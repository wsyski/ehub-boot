/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import com.axiell.ehub.provider.ContentProvider;

/**
 * A Panel that displays a ContentProviderConsumer. It also provides the possibility to modify the
 * properties of the ContentProviderConsumer.
 */
final class ContentProviderConsumerPanel extends BreadCrumbPanel {
    private final ContentProviderConsumer contentProviderConsumer;
    private final ContentProviderConsumerEditForm contentProviderConsumerForm;

    ContentProviderConsumerPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final ContentProviderConsumer contentProviderConsumer) {
        super(panelId, breadCrumbModel);
        this.contentProviderConsumer = contentProviderConsumer;
        
        final ConsumersMediator consumersMediator = new ConsumersMediator();
        consumersMediator.registerContentProviderConsumerPanel(this);
        
        contentProviderConsumerForm = new ContentProviderConsumerEditForm("cpcForm", contentProviderConsumer, consumersMediator);
        add(contentProviderConsumerForm);
    }

    @Override
    public String getTitle() {
        ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        return contentProvider.getName().toString();
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        contentProviderConsumerForm.setModelObject(contentProviderConsumer);
        super.onActivate(previous);
    }
}
