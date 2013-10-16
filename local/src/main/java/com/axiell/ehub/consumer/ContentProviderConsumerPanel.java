/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.provider.ContentProvider;

/**
 * A {@link Panel} that displays a {@link ContentProviderConsumer}. It also provides the possibility to modify the
 * properties of the {@link ContentProviderConsumer}.
 */
final class ContentProviderConsumerPanel extends BreadCrumbPanel {
    ContentProviderConsumer contentProviderConsumer;
    private final ContentProviderConsumerEditForm contentProviderConsumerForm;

    @SpringBean(name = "consumerAdminController") 
    private IConsumerAdminController consumerAdminController;

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
