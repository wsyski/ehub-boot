/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * A {@link Panel} that displays all available {@link EhubConsumer}s in the eHUB. It also provides the possibility
 * getInstance a new {@link EhubConsumer}.
 */
final class EhubConsumersPanel extends BreadCrumbPanel {
    private final WebMarkupContainer ehubConsumerFormContainer;
    private final ListView<EhubConsumer> ehubConsumersView;
    private final EhubConsumerCreateForm ehubConsumerForm;
    private final EhubConsumerCreateLink newEhubConsumerLink;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    /**
     * Constructs a new {@link EhubConsumersPanel}.
     *
     * @param panelId         the ID of this {@link Panel}
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     */
    EhubConsumersPanel(final String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        addFeedbackPanel();
        ConsumersMediator consumersMediator = new ConsumersMediator();
        consumersMediator.registerEhubConsumersPanel(this);

        ehubConsumersView = new EhubConsumerListView("ehubConsumers", breadCrumbModel, consumersMediator);
        add(ehubConsumersView);

        ehubConsumerFormContainer = makeEhubConsumerFormContainer(consumersMediator);
        add(ehubConsumerFormContainer);

        ehubConsumerForm = new EhubConsumerCreateForm("ehubConsumerForm", consumersMediator);
        ehubConsumerFormContainer.add(ehubConsumerForm);

        newEhubConsumerLink = makeNewEhubConsumerLink(consumersMediator);
        add(newEhubConsumerLink);
    }

    private EhubConsumerCreateLink makeNewEhubConsumerLink(ConsumersMediator consumersMediator) {
        EhubConsumerCreateLink link = new EhubConsumerCreateLink("newEhubConsumerLink", consumersMediator);
        consumersMediator.registerEhubConsumerCreateLink(link);
        return link;
    }

    private WebMarkupContainer makeEhubConsumerFormContainer(ConsumersMediator consumersMediator) {
        final EhubConsumerCancelLink link = new EhubConsumerCancelLink("cancelNewEhubConsumerLink", consumersMediator);
        final WebMarkupContainer container = new WebMarkupContainer("ehubConsumerFormContainer");
        container.add(link);
        container.setOutputMarkupPlaceholderTag(true);
        consumersMediator.registerEhubConsumerFormContainer(container);
        return container;
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final EhubConsumer ehubConsumer = new EhubConsumer();
        ehubConsumerForm.setEhubConsumer(ehubConsumer);
        ehubConsumerFormContainer.setVisible(false);

        final List<EhubConsumer> ehubConsumers = consumerAdminController.getEhubConsumers();
        ehubConsumersView.setList(ehubConsumers);

        newEhubConsumerLink.setVisible(true);

        super.onActivate(previous);
    }

    private void addFeedbackPanel() {
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
    }
}
