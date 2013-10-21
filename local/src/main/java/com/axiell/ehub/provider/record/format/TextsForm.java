package com.axiell.ehub.provider.record.format;

import java.util.List;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.language.Language;

class TextsForm extends StatelessForm<FormatDecoration> {
    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    TextsForm(final String id, final FormatDecorationMediator formatDecorationMediator, final FormatDecoration formatDecoration) {
	super(id);
	final IModel<FormatDecoration> formModel = new Model<>();
	setModel(formModel);
	setModelObject(formatDecoration);
	
	addTextsListView(formModel, formatDecorationMediator);
        addSaveButton(formModel, formatDecorationMediator);
    }

    private void addTextsListView(final IModel<FormatDecoration> formModel, final FormatDecorationMediator formatDecorationMediator) {
	final List<Language> languages = languageAdminController.getLanguages();
	final TextsListView textsListView = new TextsListView("texts", languages, formModel, formatDecorationMediator);
        add(textsListView);
    }

    private void addSaveButton(final IModel<FormatDecoration> formModel, final FormatDecorationMediator formatDecorationMediator) {
	final TextsSaveButton saveButton = new TextsSaveButton("submit", formModel, formatDecorationMediator);
        add(saveButton);
    }
}
