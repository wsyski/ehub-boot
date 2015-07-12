/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import com.axiell.ehub.provider.IContentProviderAdminController;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * A {@link Panel} that displays a {@link EhubConsumer}. It also provides the
 * possibility to modify the properties of the {@link EhubConsumer} and to add
 * new {@link ContentProviderConsumer}s to the {@link EhubConsumer}.
 */
final class EhubConsumerPanel extends BreadCrumbPanel {
    private EhubConsumer ehubConsumer;
    private final EhubConsumerHandler ehubConsumerHandler;
    private final ContentProviderConsumerCreateLink newContentProviderConsumerLink;
    private final EhubConsumerEditForm editEhubConsumerForm;
    private final ContentProviderConsumerListView contentProviderConsumerListView;
    private final WebMarkupContainer contentProviderConsumerCreateFormContainer;
    private final ContentProviderConsumerCreateForm contentProviderConsumerCreateForm;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    EhubConsumerPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final EhubConsumer ehubConsumer) {
        super(panelId, breadCrumbModel);
        addFeedbackPanel();
        final ConsumersMediator consumersMediator = new ConsumersMediator();
        consumersMediator.registerEhubConsumerPanel(this);

        ehubConsumerHandler = new EhubConsumerHandler();
        this.ehubConsumer = ehubConsumer;

        editEhubConsumerForm = new EhubConsumerEditForm("ehubConsumerForm", ehubConsumer, consumersMediator);
        add(editEhubConsumerForm);

        contentProviderConsumerListView = new ContentProviderConsumerListView("contentProviderConsumers", breadCrumbModel, consumersMediator);
        add(contentProviderConsumerListView);

        contentProviderConsumerCreateForm = new ContentProviderConsumerCreateForm("contentProviderConsumerForm", ehubConsumerHandler, ehubConsumer, consumersMediator);
        contentProviderConsumerCreateFormContainer = makeContentProviderConsumerCreateFormContainer(contentProviderConsumerCreateForm, consumersMediator);
        add(contentProviderConsumerCreateFormContainer);
        consumersMediator.registerContentProviderConsumerCreateFormContainer(contentProviderConsumerCreateFormContainer);

        newContentProviderConsumerLink = new ContentProviderConsumerCreateLink("newCpcLink", consumersMediator);
        consumersMediator.registerContentProviderConsumerCreateLink(newContentProviderConsumerLink);
        add(newContentProviderConsumerLink);
    }

    private WebMarkupContainer makeContentProviderConsumerCreateFormContainer(ContentProviderConsumerCreateForm form, ConsumersMediator consumersMediator) {
        final ContentProviderConsumerCancelLink link = new ContentProviderConsumerCancelLink("cancelNewCpcLink", consumersMediator);
        WebMarkupContainer container = new WebMarkupContainer("contentProviderConsumerFormContainer");
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisible(false);
        container.add(form);
        container.add(link);
        return container;
    }

    @Override
    public String getTitle() {
        return ehubConsumer.getDescription();
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        ehubConsumer = consumerAdminController.getEhubConsumer(ehubConsumer.getId());
        ehubConsumerHandler.retrieveRemainingContentProviders(contentProviderAdminController, ehubConsumer);

        newContentProviderConsumerLink.setVisible(ehubConsumerHandler.hasRemainingProviders());

        updateEditEhubConsumerForm();
        updateNewContentProviderConsumerForm();
        updateContentProviderConsumerListView();

        super.onActivate(previous);
    }

    private void updateEditEhubConsumerForm() {
        editEhubConsumerForm.setEhubConsumer(ehubConsumer);
    }

    private void updateNewContentProviderConsumerForm() {
        contentProviderConsumerCreateFormContainer.setVisible(false);
        contentProviderConsumerCreateForm.resetForm();
    }

    private void updateContentProviderConsumerListView() {
        final List<ContentProviderConsumer> contentProviderConsumers = ehubConsumer.getContentProviderConsumersAsList();
        contentProviderConsumerListView.setList(contentProviderConsumers);
    }


    private void addFeedbackPanel() {
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
    }
}
