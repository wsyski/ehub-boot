package com.axiell.ehub.language;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.DeleteLink;

final class LanguageDeleteLink extends DeleteLink<Void> {
    private static final String CONFIRMATION_TEXT = "Are you sure you want to delete this language including all format texts in this language?";
    private final Language language;
    private final LanguagesMediator mediator;
    
    @SpringBean(name = "languageAdminController") 
    private ILanguageAdminController languageAdminController;

    LanguageDeleteLink(final String id, final Language language, final LanguagesMediator mediator) {
        super(id, CONFIRMATION_TEXT);
        this.language = language;
        this.mediator = mediator;
    }

    @Override
    public void onClick() {
        languageAdminController.delete(language);
        mediator.afterDeleteLanguage();
    }
}