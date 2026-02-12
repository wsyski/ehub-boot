package com.axiell.ehub.local.language;

import com.axiell.ehub.common.language.Language;
import org.apache.wicket.markup.html.basic.Label;

import java.util.Locale;

public class LanguageLabel extends Label {

    public LanguageLabel(final String id, final Language language, final Locale locale) {
        super(id, language.getDisplayName(locale));
    }
}
