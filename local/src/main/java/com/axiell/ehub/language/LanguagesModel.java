package com.axiell.ehub.language;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Locale;

public class LanguagesModel extends LoadableDetachableModel<List<Language>> {
    private final Locale locale;

    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    public LanguagesModel(final Locale locale) {
        this.locale = locale;
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected List<Language> load() {
        final List<Language> languages = languageAdminController.getLanguages();
        return Language.sort(languages, locale);
    }
}
