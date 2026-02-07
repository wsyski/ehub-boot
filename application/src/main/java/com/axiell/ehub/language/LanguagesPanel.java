/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.language;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

final class LanguagesPanel extends BreadCrumbPanel {
    private final LanguagesListView languagesListView;
    private final LanguageForm languageForm;

    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    LanguagesPanel(final String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        final LanguagesMediator mediator = new LanguagesMediator();
        mediator.registerLanguagesPanel(this);

        languagesListView = new LanguagesListView("languages", mediator);
        add(languagesListView);

        languageForm = new LanguageForm("languageForm", mediator);
        add(languageForm);
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }

    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final List<Language> languages = languageAdminController.getLanguages();
        languagesListView.setList(languages);

        languageForm.resetForm();

        super.onActivate(previous);
    }
}
