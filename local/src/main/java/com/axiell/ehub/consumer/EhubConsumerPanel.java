/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.provider.IContentProviderAdminController;

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
    private final WebMarkupContainer newContentProviderConsumerFormContainer;
    private final NewContentProviderConsumerForm newContentProviderConsumerForm;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    EhubConsumerPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final EhubConsumer ehubConsumer) {
	super(panelId, breadCrumbModel);
	final ConsumersMediator consumersMediator = new ConsumersMediator();
	consumersMediator.registerEhubConsumerPanel(this);

	ehubConsumerHandler = new EhubConsumerHandler();
	this.ehubConsumer = ehubConsumer;

	editEhubConsumerForm = new EhubConsumerEditForm("ehubConsumerForm", ehubConsumer, consumersMediator);
	add(editEhubConsumerForm);

	contentProviderConsumerListView = new ContentProviderConsumerListView("contentProviderConsumers", breadCrumbModel, consumersMediator);
	add(contentProviderConsumerListView);

	newContentProviderConsumerForm = new NewContentProviderConsumerForm("contentProviderConsumerForm", ehubConsumerHandler, ehubConsumer, consumersMediator);
	newContentProviderConsumerFormContainer = makeNewContentProviderConsumerFormContainer(newContentProviderConsumerForm, consumersMediator);
	add(newContentProviderConsumerFormContainer);
	consumersMediator.registerContentProviderConsumerCreateFormContainer(newContentProviderConsumerFormContainer);

	newContentProviderConsumerLink = new ContentProviderConsumerCreateLink("newCpcLink", consumersMediator);
	consumersMediator.registerContentProviderConsumerCreateLink(newContentProviderConsumerLink);
	add(newContentProviderConsumerLink);
    }

    private WebMarkupContainer makeNewContentProviderConsumerFormContainer(NewContentProviderConsumerForm form, ConsumersMediator consumersMediator) {
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
	newContentProviderConsumerFormContainer.setVisible(false);
	newContentProviderConsumerForm.resetForm();
    }

    private void updateContentProviderConsumerListView() {
	final List<ContentProviderConsumer> contentProviderConsumers = ehubConsumer.getContentProviderConsumersAsList();
	contentProviderConsumerListView.setList(contentProviderConsumers);
    }
}
