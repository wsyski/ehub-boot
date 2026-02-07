package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

final class PlatformsListView extends ListView<Platform> {
    private final PlatformsMediator mediator;

    PlatformsListView(final String id, final PlatformsMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    protected void populateItem(final ListItem<Platform> item) {
        final Platform platform = item.getModelObject();
        final PlatformUpdateFormPanel platformUpdateFormPanel = new PlatformUpdateFormPanel("platformUpdateFormPanel", platform, mediator);
        item.add(platformUpdateFormPanel);
        final Link<Void> deleteLink = new PlatformDeleteLink("deleteLink", platform, mediator);
        item.add(deleteLink);
    }
}