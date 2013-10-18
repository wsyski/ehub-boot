/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public final class FormatDecorationPanel extends BreadCrumbPanel {
    private final FormatDecorationEditFormPanel decorationFormPanel;
    private FormatDecoration formatDecoration;
    private final TextsForm textsForm;

    @SpringBean(name = "formatAdminController") 
    private IFormatAdminController formatAdminController;
    
    public FormatDecorationPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final FormatDecoration formatDecoration) {
        super(panelId, breadCrumbModel);
        final FormatDecorationMediator formatDecorationMediator = new FormatDecorationMediator();
        formatDecorationMediator.registerFormatDecorationPanel(this);
        
        this.formatDecoration = formatDecoration;
        
        decorationFormPanel = new FormatDecorationEditFormPanel("decorationFormPanel", formatDecorationMediator, formatDecoration);
        add(decorationFormPanel);

        textsForm = new TextsForm("textsForm", formatDecorationMediator);
        add(textsForm);
    }

    @Override
    public String getTitle() {
        return formatDecoration.getContentProviderFormatId();
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        formatDecoration = formatAdminController.getFormatDecoration(formatDecoration.getId());
        textsForm.setModelObject(formatDecoration);

        decorationFormPanel.setFormModelObject(formatDecoration);
        super.onActivate(previous);
    }
}
