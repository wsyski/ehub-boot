package com.axiell.ehub.language;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

class LanguageForm extends StatelessForm<Language> {

    LanguageForm(final String id, final LanguagesMediator mediator) {
	super(id);
	final IModel<Language> formModel = new Model<>();
	setModel(formModel);

	addLanguageIdField(formModel);
	addSaveButton(mediator, formModel);
    }

    private void addLanguageIdField(final IModel<Language> formModel) {
	final LanguageIdModel idModel = new LanguageIdModel(formModel);
	final RequiredTextField<String> languageIdField = new RequiredTextField<>("id", idModel);
	add(languageIdField);
    }

    private void addSaveButton(final LanguagesMediator mediator, final IModel<Language> formModel) {
	final LanguageSaveButton saveButton = new LanguageSaveButton("submit", formModel, mediator);
	add(saveButton);
    }

    void resetForm() {
	final Language language = new Language();
	setModelObject(language);
    }
}
