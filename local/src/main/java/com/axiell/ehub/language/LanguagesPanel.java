/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.language;

import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * A {@link Panel} that displays all supported {@link Language}s in the eHUB. It also provides the possibility to create
 * a new language or to delete an existing language.
 */
public final class LanguagesPanel extends BreadCrumbPanel {
    private static final long serialVersionUID = -7977142084053043064L;
    private final ListView<Language> languagesView;
    private final StatelessForm<Language> languageForm;

    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    /**
     * Constructs a new {@link LanguagesPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     */
    public LanguagesPanel(final String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        this.languagesView = new ListView<Language>("languages") {
            private static final long serialVersionUID = 5788487384958033295L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<Language> item) {
                final Language language = item.getModelObject();

                final IModel<String> languageModel = new PropertyModel<>(language, "id");
                final TextField<String> languageField = new TextField<>("language", languageModel);
                languageField.setEnabled(false);
                item.add(languageField);

                final Link<Void> deleteLink = new Link<Void>("deleteLink") {
                    private static final long serialVersionUID = 5550837870796648954L;

                    /**
                     * @see org.apache.wicket.markup.html.link.Link#onClick()
                     */
                    @Override
                    public void onClick() {
                        languageAdminController.delete(language);
                        activate(LanguagesPanel.this);
                    }
                };
                item.add(deleteLink);
            }
        };
        add(languagesView);

        this.languageForm = createLanguageForm();
        add(languageForm);
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
        final List<Language> languages = languageAdminController.getLanguages();
        languagesView.setList(languages);

        final Language language = new Language();
        final IModel<Language> languageFormModel = new CompoundPropertyModel<>(language);
        languageForm.setModel(languageFormModel);

        super.onActivate(previous);
    }

    /**
     * Creates a {@link StatelessForm} that provides the possibility to create a new {@link Language}.
     * 
     * @return a {@link StatelessForm}
     */
    private StatelessForm<Language> createLanguageForm() {
        final StatelessForm<Language> languageForm = new StatelessForm<>("languageForm");

        final RequiredTextField<String> languageField = new RequiredTextField<>("id");
        languageForm.add(languageField);

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = 2097250398008883845L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                final Language language = languageForm.getModelObject();
                languageAdminController.save(language);
                activate(LanguagesPanel.this);
            }
        };
        languageForm.add(submitButton);

        return languageForm;
    }
}
