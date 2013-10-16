package com.axiell.ehub.provider;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

final class ContentProvidersListView extends ListView<ContentProvider> {
    private final IBreadCrumbModel breadCrumbModel;

    ContentProvidersListView(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id);
        this.breadCrumbModel = breadCrumbModel;
    }

    @Override
    protected void populateItem(ListItem<ContentProvider> item) {
        final ContentProvider contentProvider = item.getModelObject();
        final BreadCrumbPanelLink link = makeContentProviderLink(contentProvider);        
        item.add(link);
    }

    private BreadCrumbPanelLink makeContentProviderLink(final ContentProvider contentProvider) {
	final IBreadCrumbPanelFactory factory = new ContentProviderPanelFactory(contentProvider);
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("contentProviderLink", breadCrumbModel, factory);
        final Label linkLabel = makeContentProviderLinkLabel(contentProvider);
        link.add(linkLabel);
	return link;
    }

    private Label makeContentProviderLinkLabel(final ContentProvider contentProvider) {
	final String name = contentProvider.getName().toString();
        return new Label("contentProviderLinkLabel", name);
    }
}