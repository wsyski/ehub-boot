/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.feedback.EhubFeedbackPanel;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecorationCreateFormPanel;
import com.axiell.ehub.provider.record.format.FormatDecorationCreateLink;
import com.axiell.ehub.provider.record.format.FormatDecorationsListView;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class ContentProviderPanel extends BreadCrumbPanel {
    private final String contentProviderName;
    private final ContentProviderEditForm contentProviderForm;
    private final FormatDecorationsListView formatDecorationsListView;

    private final IndicatingAjaxFallbackLink<Void> formatDecorationCreateLink;
    private final FormatDecorationCreateFormPanel formatDecorationCreateFormPanel;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    ContentProviderPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final String contentProviderName) {
        super(panelId, breadCrumbModel);
        final ContentProviderMediator contentProviderMediator = new ContentProviderMediator();
        contentProviderMediator.registerContentProviderPanel(this);

        this.contentProviderName = contentProviderName;
        contentProviderForm = new ContentProviderEditForm("contentProviderForm", contentProviderMediator);
        add(contentProviderForm);

        formatDecorationsListView = new FormatDecorationsListView("decorations", breadCrumbModel, contentProviderMediator);
        add(formatDecorationsListView);

        addFeedbackPanel();

        formatDecorationCreateFormPanel = new FormatDecorationCreateFormPanel("decorationFormPanel", contentProviderMediator);
        contentProviderMediator.registerFormatDecorationCreateFormPanel(formatDecorationCreateFormPanel);
        add(formatDecorationCreateFormPanel);

        formatDecorationCreateLink = makeFormatDecorationCreateLink(contentProviderMediator);
        add(formatDecorationCreateLink);

    }

    private void addFeedbackPanel() {
        EhubFeedbackPanel feedback = new EhubFeedbackPanel("feedback");
        add(feedback);
    }

    private FormatDecorationCreateLink makeFormatDecorationCreateLink(final ContentProviderMediator contentProviderMediator) {
        FormatDecorationCreateLink link = new FormatDecorationCreateLink("newFormatDecorationLink", contentProviderMediator);
        link.setOutputMarkupPlaceholderTag(true);
        contentProviderMediator.registerFormatDecorationCreateLink(link);
        return link;
    }

    @Override
    public String getTitle() {
        return contentProviderName.toString();
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final ContentProvider contentProvider = contentProviderAdminController.getContentProvider(contentProviderName);

        contentProviderForm.setContentProvider(contentProvider);

        formatDecorationsListView.setContentProvider(contentProvider);

        updateFormatDecorationCreateFormPanel(contentProvider);

        formatDecorationCreateLink.setVisible(true);

        super.onActivate(previous);
    }

    private void updateFormatDecorationCreateFormPanel(ContentProvider contentProvider) {
        final FormatDecoration formatDecoration = new FormatDecoration(contentProvider);
        formatDecorationCreateFormPanel.setFormModelObject(formatDecoration);
        formatDecorationCreateFormPanel.setVisible(false);
    }
}
