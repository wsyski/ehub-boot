package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.local.provider.alias.AliasesPanelFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * A Panel that displays all available {@link ContentProvider}s in the eHUB.
 */
final class ContentProvidersPanel extends BreadCrumbPanel {
    private final ContentProvidersListView contentProvidersView;

    private final WebMarkupContainer contentProviderFormContainer;
    private final ContentProviderCreateForm contentProviderForm;
    private final ContentProviderCreateLink newContentProviderLink;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    ContentProvidersPanel(String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        ContentProvidersMediator contentProvidersMediator = new ContentProvidersMediator();
        contentProvidersMediator.registerContentProvidersPanel(this);
        this.contentProvidersView = new ContentProvidersListView("contentProviders", breadCrumbModel, contentProvidersMediator);
        add(contentProvidersView);
        addFeedbackPanel();
        addAliasesLink(breadCrumbModel);

        contentProviderFormContainer = makeContentProviderFormContainer(contentProvidersMediator);
        add(contentProviderFormContainer);

        contentProviderForm = new ContentProviderCreateForm("contentProviderForm", contentProvidersMediator);
        contentProviderFormContainer.add(contentProviderForm);

        newContentProviderLink = makeNewContentProviderLink(contentProvidersMediator);
        add(newContentProviderLink);
    }

    private ContentProviderCreateLink makeNewContentProviderLink(final ContentProvidersMediator contentProvidersMediator) {
        ContentProviderCreateLink link = new ContentProviderCreateLink("newContentProviderLink", contentProvidersMediator);
        contentProvidersMediator.registerContentProviderCreateLink(link);
        return link;
    }

    private WebMarkupContainer makeContentProviderFormContainer(final ContentProvidersMediator consumersMediator) {
        final ContentProviderCancelLink link = new ContentProviderCancelLink("cancelNewContentProviderLink", consumersMediator);
        final WebMarkupContainer container = new WebMarkupContainer("contentProviderFormContainer");
        container.add(link);
        container.setOutputMarkupPlaceholderTag(true);
        consumersMediator.registerContentProviderFormContainer(container);
        return container;
    }

    private void addAliasesLink(final IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new AliasesPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("aliasesLink", breadCrumbModel, factory);
        add(link);
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }

    private void addFeedbackPanel() {
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final ContentProvider contentProvider = new ContentProvider();
        contentProviderForm.setContentProvider(contentProvider);
        contentProviderFormContainer.setVisible(false);
        newContentProviderLink.setVisible(true);
        final List<ContentProvider> contentProviders = contentProviderAdminController.getContentProviders();
        contentProvidersView.setList(contentProviders);
        super.onActivate(previous);
    }
}
