package com.axiell.ehub.local.language;

import com.axiell.ehub.common.language.Language;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

final class LanguagesListView extends ListView<Language> {
    private final LanguagesMediator mediator;

    LanguagesListView(final String id, final LanguagesMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    protected void populateItem(ListItem<Language> item) {
        final Language language = item.getModelObject();

        final IModel<String> languageModel = new PropertyModel<>(language, "id");
        final TextField<String> languageField = new TextField<>("language", languageModel);
        languageField.setEnabled(false);
        item.add(languageField);

        final Link<Void> deleteLink = new LanguageDeleteLink("deleteLink", language, mediator);
        item.add(deleteLink);
    }
}
