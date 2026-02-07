/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import com.axiell.ehub.feedback.EhubFeedbackPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import com.axiell.ehub.provider.ContentProvider;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A Panel that displays a ContentProviderConsumer. It also provides the possibility to modify the
 * properties of the ContentProviderConsumer.
 */
final class ContentProviderConsumerPanel extends BreadCrumbPanel {
    private final ContentProviderConsumer contentProviderConsumer;
    private final ContentProviderConsumerEditForm contentProviderConsumerForm;

    ContentProviderConsumerPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final ContentProviderConsumer contentProviderConsumer) {
        super(panelId, breadCrumbModel);
        addFeedbackPanel();
        this.contentProviderConsumer = contentProviderConsumer;

        final ConsumersMediator consumersMediator = new ConsumersMediator();
        consumersMediator.registerContentProviderConsumerPanel(this);

        contentProviderConsumerForm = new ContentProviderConsumerEditForm("cpcForm", contentProviderConsumer, consumersMediator);
        add(contentProviderConsumerForm);
    }

    @Override
    public IModel<String> getTitle() {
        ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        return new Model<>(contentProvider.getName());
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        contentProviderConsumerForm.setModelObject(contentProviderConsumer);
        super.onActivate(previous);
    }

    private void addFeedbackPanel() {
        EhubFeedbackPanel feedback = new EhubFeedbackPanel("feedback");
        add(feedback);
    }
}
