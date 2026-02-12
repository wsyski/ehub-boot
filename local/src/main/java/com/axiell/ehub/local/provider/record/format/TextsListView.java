package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.language.LanguageLabel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

final class TextsListView extends ListView<Language> {
    private IModel<FormatDecoration> formModel;
    private final FormatDecorationMediator formatDecorationMediator;

    TextsListView(final String id, final List<Language> languages, final IModel<FormatDecoration> formModel, final FormatDecorationMediator formatDecorationMediator) {
        super(id, languages);
        this.formModel = formModel;
        this.formatDecorationMediator = formatDecorationMediator;
    }

    @Override
    protected void populateItem(ListItem<Language> item) {
        final Language language = item.getModelObject();

        final LanguageLabel languageLabel = new LanguageLabel("language", language, getLocale());
        item.add(languageLabel);

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

    private TextField<String> makeNameField(final Language language) {
        final IModel<String> nameModel = new NameModel(formModel, language);
        return new TextField<>("name", nameModel);
    }

    private TextField<String> makeDescriptionField(final Language language) {
        final IModel<String> descriptionModel = new DescriptionModel(formModel, language);
        return new TextField<>("description", descriptionModel);
    }
}
