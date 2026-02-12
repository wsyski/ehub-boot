/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public final class FormatDecorationPanel extends BreadCrumbPanel {
    private final FormatDecorationEditFormPanel decorationFormPanel;
    private FormatDecoration formatDecoration;
    private final FormatDecorationMediator formatDecorationMediator;

    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    public FormatDecorationPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final FormatDecoration formatDecoration) {
        super(panelId, breadCrumbModel);
        formatDecorationMediator = new FormatDecorationMediator();
        formatDecorationMediator.registerFormatDecorationPanel(this);

        this.formatDecoration = formatDecoration;

        decorationFormPanel = new FormatDecorationEditFormPanel("decorationFormPanel", formatDecorationMediator, formatDecoration);
        add(decorationFormPanel);

        addOrReplaceTextsForm();
    }

    private void addOrReplaceTextsForm() {
        final TextsForm textsForm = new TextsForm("textsForm", formatDecorationMediator, formatDecoration);
        addOrReplace(textsForm);
    }

    @Override
    public IModel<String> getTitle() {
        return new Model<>(formatDecoration.getContentProviderFormatId());
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        formatDecoration = formatAdminController.getFormatDecoration(formatDecoration.getId());
        addOrReplaceTextsForm();

        decorationFormPanel.setFormModelObject(formatDecoration);
        super.onActivate(previous);
    }
}
