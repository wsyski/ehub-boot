package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

final class ContentProvidersListView extends ListView<ContentProvider> {
    private final IBreadCrumbModel breadCrumbModel;
    private final ContentProvidersMediator contentProvidersMediator;

    ContentProvidersListView(final String id, final IBreadCrumbModel breadCrumbModel, final ContentProvidersMediator contentProvidersMediator) {
        super(id);
        this.breadCrumbModel = breadCrumbModel;
        this.contentProvidersMediator = contentProvidersMediator;
    }

    @Override
    protected void populateItem(ListItem<ContentProvider> item) {
        final ContentProvider contentProvider = item.getModelObject();
        final BreadCrumbPanelLink editLink = makeContentProviderLink(contentProvider);
        item.add(editLink);
        addDeleteLink(item, contentProvider);
    }

    private BreadCrumbPanelLink makeContentProviderLink(final ContentProvider contentProvider) {
        final IBreadCrumbPanelFactory factory = new ContentProviderPanelFactory(contentProvider);
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("contentProviderLink", breadCrumbModel, factory);
        final Label linkLabel = makeContentProviderLinkLabel(contentProvider);
        link.add(linkLabel);
        return link;
    }

    private Label makeContentProviderLinkLabel(final ContentProvider contentProvider) {
        final String name = contentProvider.getName();
        return new Label("contentProviderLinkLabel", name);
    }

    private void addDeleteLink(final ListItem<ContentProvider> item, final ContentProvider contentProvider) {
        final Link<ContentProvider> deleteLink = new ContentProviderDeleteLink("deleteLink", contentProvider, contentProvidersMediator);
        item.add(deleteLink);
    }
}
