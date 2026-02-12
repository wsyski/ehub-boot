package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

class ContentProviderConsumerListView extends ListView<ContentProviderConsumer> {
    private final IBreadCrumbModel breadCrumbModel;
    private final ConsumersMediator consumersMediator;

    ContentProviderConsumerListView(final String id, final IBreadCrumbModel breadCrumbModel, final ConsumersMediator consumersMediator) {
        super(id);
        this.breadCrumbModel = breadCrumbModel;
        this.consumersMediator = consumersMediator;
    }

    @Override
    protected void populateItem(final ListItem<ContentProviderConsumer> item) {
        final ContentProviderConsumer contentProviderConsumer = item.getModelObject();

        final BreadCrumbPanelLink contentProviderConsumerLink = makeContentProviderConsumerLink(contentProviderConsumer);
        item.add(contentProviderConsumerLink);

        final Link<ContentProviderConsumer> deleteLink = new ContentProviderConsumerDeleteLink("deleteLink", contentProviderConsumer, consumersMediator);
        item.add(deleteLink);
    }

    private BreadCrumbPanelLink makeContentProviderConsumerLink(final ContentProviderConsumer contentProviderConsumer) {
        final IBreadCrumbPanelFactory factory = new ContentProviderConsumerPanelFactory(contentProviderConsumer);
        final BreadCrumbPanelLink contentProviderConsumerLink = new BreadCrumbPanelLink("contentProviderConsumerLink", breadCrumbModel, factory);
        final Label contentProviderConsumerLinkLabel = makeContentProviderConsumerLinkLabel(contentProviderConsumer);
        contentProviderConsumerLink.add(contentProviderConsumerLinkLabel);
        return contentProviderConsumerLink;
    }

    private Label makeContentProviderConsumerLinkLabel(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String contentProviderName = contentProvider.getName();
        return new Label("contentProviderConsumerLinkLabel", contentProviderName);
    }
}
