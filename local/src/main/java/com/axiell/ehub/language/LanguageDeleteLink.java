package com.axiell.ehub.language;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.ConfirmationLink;

final class LanguageDeleteLink extends ConfirmationLink<Void> {
    private final Language language;
    private final LanguagesMediator mediator;
    
    @SpringBean(name = "languageAdminController") 
    private ILanguageAdminController languageAdminController;

    LanguageDeleteLink(final String id, final Language language, final LanguagesMediator mediator) {
        super(id);
        this.language = language;
        this.mediator = mediator;
    }

    @Override
    public void onClick() {
        languageAdminController.delete(language);
        mediator.afterDeleteLanguage();
    }
}