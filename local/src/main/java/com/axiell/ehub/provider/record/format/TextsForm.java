package com.axiell.ehub.provider.record.format;

import java.util.List;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.provider.ContentProviderMediator;

class TextsForm extends StatelessForm<FormatDecoration> {
    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    TextsForm(final String id, final ContentProviderMediator contentProviderMediator) {
	super(id);
	final IModel<FormatDecoration> formModel = new Model<>();
	setModel(formModel);
	
	addTextsListView(formModel, contentProviderMediator);
        addSaveButton(formModel, contentProviderMediator);
    }

    private void addTextsListView(final IModel<FormatDecoration> formModel, final ContentProviderMediator contentProviderMediator) {
	final List<Language> languages = languageAdminController.getLanguages();
        final ListView<Language> textsListView = new TextsListView("texts", languages, formModel, contentProviderMediator);
        add(textsListView);
    }

    private void addSaveButton(final IModel<FormatDecoration> formModel, final ContentProviderMediator contentProviderMediator) {
	final TextsSaveButton saveButton = new TextsSaveButton("submit", formModel, contentProviderMediator);
        add(saveButton);
    }
    
    void setFormatDecoration(final FormatDecoration formatDecoration) {
	setModelObject(formatDecoration);
    }
}
