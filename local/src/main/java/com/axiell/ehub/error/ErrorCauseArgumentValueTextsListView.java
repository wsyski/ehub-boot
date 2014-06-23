package com.axiell.ehub.error;


import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.language.LanguageLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

class ErrorCauseArgumentValueTextsListView extends ListView<Language> {
    private final IModel<ErrorCauseArgumentValue> formModel;

    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    ErrorCauseArgumentValueTextsListView(final String id, final IModel<ErrorCauseArgumentValue> formModel) {
        super(id);
        this.formModel = formModel;
        setLanguages();
    }

    private void setLanguages() {
        final List<Language> languages = languageAdminController.getLanguages();
        setList(languages);
    }

    @Override
    protected void populateItem(final ListItem<Language> item) {
        final Language language = item.getModelObject();

        final LanguageLabel languageLabel = new LanguageLabel("language", language, getLocale());
        item.add(languageLabel);

        final ErrorCauseArgumentValueTextField valueTextField = makeErrorCauseArgumentValueTextField(language);
        item.add(valueTextField);
    }

    private ErrorCauseArgumentValueTextField makeErrorCauseArgumentValueTextField(final Language language) {
        final IModel<String> textModel = new ErrorCauseArgumentValueTextModel(formModel, language);
        return new ErrorCauseArgumentValueTextField("text", textModel);
    }
}
