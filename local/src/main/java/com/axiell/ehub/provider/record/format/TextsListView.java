package com.axiell.ehub.provider.record.format;

import java.util.List;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.axiell.ehub.language.Language;

final class TextsListView extends ListView<Language> {
    private IModel<FormatDecoration> formModel;
//    private final IModel<FormatDecoration> formModel;
    private final FormatDecorationMediator formatDecorationMediator;

    TextsListView(final String id, final List<? extends Language> languages, final IModel<FormatDecoration> formModel, final FormatDecorationMediator formatDecorationMediator) {
        super(id, languages);
        this.formModel = formModel;
        this.formatDecorationMediator = formatDecorationMediator;
    }
    
//    TextsListView(final String id, final List<? extends Language> languages, final FormatDecorationMediator formatDecorationMediator) {
//      super(id, languages);
////      this.formModel = formModel;
//      this.formatDecorationMediator = formatDecorationMediator;
//  }

    @Override
    protected void populateItem(ListItem<Language> item) {
        final Language language = item.getModelObject();

        final TextField<String> languageField = makeLanguageField(language);
        item.add(languageField);

        final TextField<String> nameField = makeNameField(language);
        item.add(nameField);

        final TextField<String> descriptionField = makeDescriptionField(language);
        item.add(descriptionField);

        final TextsDeleteLink deleteLink = new TextsDeleteLink("deleteLink", language, formModel, formatDecorationMediator);
        item.add(deleteLink);
    }
    
    void setFormModel(IModel<FormatDecoration> formModel) {
	this.formModel = formModel;
    }

    private TextField<String> makeLanguageField(final Language language) {
	final String languageId = language.getId();
	final IModel<String> languageModel = new Model<>(languageId);
        final TextField<String> languageField = new TextField<>("language", languageModel);
        languageField.setEnabled(false);
	return languageField;
    }

    private TextField<String> makeNameField(final Language language) {
	final IModel<String> nameModel = new NameModel(formModel, language);
        return new TextField<>("name", nameModel);
    }

    private TextField<String> makeDescriptionField(final Language language) {
	final IModel<String> descriptionModel = new DescriptionModel(formModel, language);
        return new TextField<>("description", descriptionModel);
    }
}