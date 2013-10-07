/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * A Panel that displays all available {@link ContentProvider}s in the eHUB.
 */
public final class ContentProvidersPanel extends BreadCrumbPanel {
    private static final long serialVersionUID = -6803834402491511228L;
    private final ListView<ContentProvider> contentProvidersView;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    /**
     * Constructs a new {@link ContentProvidersPanel}.
     * 
     * @param panelId the ID of this Panel
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     */
    public ContentProvidersPanel(String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        this.contentProvidersView = new ListView<ContentProvider>("contentProviders") {
            private static final long serialVersionUID = -1881085092911236653L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<ContentProvider> item) {
                final ContentProvider contentProvider = item.getModelObject();
                final String name = contentProvider.getName().toString();
                final Label linkLabel = new Label("contentProviderLinkLabel", name);
                final IBreadCrumbPanelFactory factory = createContentProviderPanelFactory(breadCrumbModel, contentProvider);
                final BreadCrumbPanelLink link = new BreadCrumbPanelLink("contentProviderLink", breadCrumbModel, factory);
                link.add(linkLabel);
                item.add(link);
            }
        };
        add(contentProvidersView);
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant#getTitle()
     */
    @Override
    public String getTitle() {
        final StringResourceModel model = new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
        return model.getString();
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel#onActivate(org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant)
     */
    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final List<ContentProvider> contentProviders = contentProviderAdminController.getContentProviders();
        contentProvidersView.setList(contentProviders);
        super.onActivate(previous);
    }

    /**
     * Creates an {@link IBreadCrumbPanelFactory} that provides the possibility to create {@link ContentProviderPanel}s.
     * 
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param contentProvider the {@link ContentProvider} to be forwarded to the {@link ContentProviderPanel}
     * @return an {@link IBreadCrumbPanelFactory}
     */
    private IBreadCrumbPanelFactory createContentProviderPanelFactory(final IBreadCrumbModel breadCrumbModel, final ContentProvider contentProvider) {
        return new IBreadCrumbPanelFactory() {
            private static final long serialVersionUID = -3267292005282282326L;

            /**
             * @see org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory#create(java.lang.String,
             * org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel)
             */
            @Override
            public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
                final ContentProviderName contentProviderName = contentProvider.getName();
                return new ContentProviderPanel(id, model, contentProviderName);
            }
        };
    }
}
