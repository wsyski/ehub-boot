package com.axiell.ehub.language;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import java.util.Locale;

public class LanguageChoice extends DropDownChoice<Language> {

    public LanguageChoice(final String id, final IModel<Language> languageModel) {
        super(id);
        setChoices();
        setChoiceRenderer();
        setModel(languageModel);
    }

    @Override
    public boolean isNullValid() {
        return false;
    }

    @Override
    protected String getNullValidKey() {
        return "txtPleaseSelectLanguage";
    }

    private void setChoiceRenderer() {
        final Locale locale = getLocale();
        final LanguageChoiceRenderer choiceRenderer = new LanguageChoiceRenderer(locale);
        setChoiceRenderer(choiceRenderer);
    }

    private void setChoices() {
        final Locale locale = getLocale();
        final LanguagesModel languagesModel = new LanguagesModel(locale);
        setChoices(languagesModel);
    }
}
