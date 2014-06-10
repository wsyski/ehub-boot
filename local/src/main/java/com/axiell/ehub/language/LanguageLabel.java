package com.axiell.ehub.language;

import org.apache.wicket.markup.html.basic.Label;

import java.util.Locale;

public class LanguageLabel extends Label {

    public LanguageLabel(final String id, final Language language, final Locale locale) {
        super(id, language.getDisplayName(locale));
    }
}
