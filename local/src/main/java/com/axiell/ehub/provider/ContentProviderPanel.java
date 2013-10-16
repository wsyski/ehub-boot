/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecorationCreateLink;
import com.axiell.ehub.provider.record.format.FormatDecorationFormPanel;
import com.axiell.ehub.provider.record.format.FormatDecorationsListView;

/**
 * A {@link Panel} that displays a {@link ContentProvider}. It also provides the possibility to modify the properties of
 * the {@link ContentProvider} and to add new {@link FormatDecoration}s to the {@link ContentProvider}.
 */
final class ContentProviderPanel extends BreadCrumbPanel {
    private final ContentProviderName contentProviderName;
    private final ContentProviderEditForm contentProviderForm;
    private final FormatDecorationsListView decorationsListView;
    private ContentProvider contentProvider;
    
    private final IndicatingAjaxFallbackLink<Void> formatDecorationCreateLink;
    private final FormatDecorationFormPanel decorationFormPanel;

    @SpringBean(name = "contentProviderAdminController") 
    private IContentProviderAdminController contentProviderAdminController;
    
    /**
     * Constructs a new {@link ContentProviderPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param contentProviderName the name of the {@link ContentProvider} to be shown
     */
    ContentProviderPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final ContentProviderName contentProviderName) {
        super(panelId, breadCrumbModel);
        final ContentProviderMediator contentProviderMediator = new ContentProviderMediator();
        contentProviderMediator.registerContentProviderPanel(this);
        
        this.contentProviderName = contentProviderName;
        contentProviderForm = new ContentProviderEditForm("contentProviderForm", contentProviderMediator);
        add(contentProviderForm);
                
        decorationsListView = new FormatDecorationsListView("decorations", breadCrumbModel, contentProviderMediator);
        add(decorationsListView);
        
        decorationFormPanel = new FormatDecorationFormPanel("decorationFormPanel", true, contentProviderMediator);
        contentProviderMediator.registerFormatDecorationFormPanel(decorationFormPanel);
        add(decorationFormPanel);
        
        formatDecorationCreateLink = makeFormatDecorationCreateLink(contentProviderMediator);
        add(formatDecorationCreateLink);
        decorationFormPanel.setOnCancelVisibleLink(formatDecorationCreateLink);
        
    }

    private FormatDecorationCreateLink makeFormatDecorationCreateLink(final ContentProviderMediator contentProviderMediator) {
	FormatDecorationCreateLink link = new FormatDecorationCreateLink("newFormatDecorationLink", contentProviderMediator);
	link.setOutputMarkupPlaceholderTag(true);
	return link;
    }
    
    @Override
    public String getTitle() {
        return contentProviderName.toString();
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        contentProvider = contentProviderAdminController.getContentProvider(contentProviderName);
        contentProviderForm.setContentProvider(contentProvider);

        decorationsListView.setContentProvider(contentProvider);
        
        final FormatDecoration formatDecoration = new FormatDecoration(contentProvider);
        decorationFormPanel.setFormModelObject(formatDecoration);
        decorationFormPanel.setVisible(false);
        formatDecorationCreateLink.setVisible(true);
        
        super.onActivate(previous);
    }
}
