package com.axiell.ehub.local.language;

import com.axiell.ehub.common.language.Language;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

import java.util.Locale;

public class LanguageChoiceRenderer extends ChoiceRenderer<Language> {
    private final Locale locale;

    public LanguageChoiceRenderer(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public String getIdValue(final Language language, int index) {
        return language.getId();
    }

    @Override
    public Object getDisplayValue(final Language language) {
        return language.getDisplayName(locale);
    }
}
