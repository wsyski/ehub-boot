package com.axiell.ehub.consumer;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

final class EhubConsumerListView extends ListView<EhubConsumer> {
    private final IBreadCrumbModel breadCrumbModel;
    private final ConsumersMediator consumersMediator;

    EhubConsumerListView(final String id, final IBreadCrumbModel breadCrumbModel,final ConsumersMediator consumersMediator) {
        super(id);
        this.breadCrumbModel = breadCrumbModel;
        this.consumersMediator = consumersMediator;
    }

    @Override
    protected void populateItem(ListItem<EhubConsumer> item) {
        final EhubConsumer ehubConsumer = item.getModelObject();
        addEhubConsumerLink(item, ehubConsumer);
        addDeleteLink(item, ehubConsumer);
    }

    private void addEhubConsumerLink(ListItem<EhubConsumer> item, final EhubConsumer ehubConsumer) {	
        final BreadCrumbPanelLink ehubConsumerLink = makeEhubConsumerLink(ehubConsumer);        
        item.add(ehubConsumerLink);
    }

    private BreadCrumbPanelLink makeEhubConsumerLink(final EhubConsumer ehubConsumer) {
	final Label linkLabel = makeEhubConsumerLinkLabel(ehubConsumer);
	final IBreadCrumbPanelFactory factory = new EhubConsumerPanelFactory(ehubConsumer);
        final BreadCrumbPanelLink ehubConsumerLink = new BreadCrumbPanelLink("ehubConsumerLink", breadCrumbModel, factory);
        ehubConsumerLink.add(linkLabel);
	return ehubConsumerLink;
    }

    private Label makeEhubConsumerLinkLabel(final EhubConsumer ehubConsumer) {
	final String description = ehubConsumer.getDescription();
        return new Label("ehubConsumerLinkLabel", description);
    }

    private void addDeleteLink(ListItem<EhubConsumer> item, final EhubConsumer ehubConsumer) {
	final Link<EhubConsumer> deleteLink = new EhubConsumerDeleteLink("deleteLink", ehubConsumer, consumersMediator);
        item.add(deleteLink);
    }
}