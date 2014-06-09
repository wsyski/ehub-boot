package com.axiell.ehub.language;

import com.google.common.collect.Lists;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LanguagesModel extends LoadableDetachableModel<List<Language>> {
    private final Locale locale;

    @SpringBean(name="languageAdminController")
    private ILanguageAdminController languageAdminController;

    public LanguagesModel(final Locale locale) {
        this.locale = locale;
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected List<Language> load() {
        final List<Language> languages = languageAdminController.getLanguages();
        return getLanguagesListSortedBy(languages);
    }

    private List<Language> getLanguagesListSortedBy(List<Language> languages) {
        List<Language> sortedLanguages = Lists.newArrayList(languages);
        Collections.sort(sortedLanguages, new LocaleAwareLanguageComparator());
        return sortedLanguages;
    }

    private class LocaleAwareLanguageComparator implements Comparator<Language> {
        @Override
        public int compare(final Language language1, final Language language2) {
            return language1.getDisplayName(locale).compareTo(language2.getDisplayName(locale));
        }
    }
}
