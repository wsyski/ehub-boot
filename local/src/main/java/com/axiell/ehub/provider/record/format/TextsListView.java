package com.axiell.ehub.provider.record.format;

import java.util.List;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.axiell.ehub.language.Language;
import com.axiell.ehub.provider.ContentProviderMediator;

final class TextsListView extends ListView<Language> {
    private final IModel<FormatDecoration> formModel;
    private final ContentProviderMediator contentProviderMediator;

    TextsListView(final String id, final List<? extends Language> list, final IModel<FormatDecoration> formModel, final ContentProviderMediator contentProviderMediator) {
        super(id, list);
        this.formModel = formModel;
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    protected void populateItem(ListItem<Language> item) {
        final Language language = item.getModelObject();

        final TextField<String> languageField = makeLanguageField(language);
        item.add(languageField);

        final TextField<String> nameField = makeNameField(language);
        item.add(nameField);

        final TextField<String> descriptionField = makeDescriptionField(language);
        item.add(descriptionField);

        final TextsDeleteLink deleteLink = new TextsDeleteLink("deleteLink", language, formModel, contentProviderMediator);
        item.add(deleteLink);
    }

    private TextField<String> makeLanguageField(final Language language) {
	final IModel<String> languageModel = new Model<>(language.getId());
        final TextField<String> languageField = new TextField<>("language", languageModel);
        languageField.setEnabled(false);
	return languageField;
    }

    private TextField<String> makeNameField(final Language language) {
	final IModel<String> nameModel = new NameModel(formModel, language);
        final TextField<String> nameField = new TextField<>("name", nameModel);
	return nameField;
    }

    private TextField<String> makeDescriptionField(final Language language) {
	final IModel<String> descriptionModel = new DescriptionModel(formModel, language);
        final TextField<String> descriptionField = new TextField<>("description", descriptionModel);
	return descriptionField;
    }
}