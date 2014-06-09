/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.language;

import java.util.List;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IEhubConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * Default implementation of the {@link ILanguageAdminController}.
 */
public class LanguageAdminController implements ILanguageAdminController {
    @Autowired
    private ILanguageRepository languageRepository;

    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IEhubConsumerRepository ehubConsumerRepository;
    
    /**
     * @see com.axiell.ehub.language.ILanguageAdminController#getLanguages()
     */
    @Override
    @Transactional(readOnly = true)
    public List<Language> getLanguages() {
        return languageRepository.findAllOrderedByLanguage();
    }
    
    /**
     * @see com.axiell.ehub.language.ILanguageAdminController#save(com.axiell.ehub.language.Language)
     */
    @Override
    @Transactional(readOnly = false)
    public Language save(Language language) {
        return languageRepository.save(language);
    }
    
    /**
     * @see com.axiell.ehub.language.ILanguageAdminController#delete(com.axiell.ehub.language.Language)
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(final Language language) throws LanguageReferencedException {
        List<EhubConsumer> ehubConsumers=ehubConsumerRepository.findByDefaultLanguage(language);
        if (!ehubConsumers.isEmpty()) {
           throw new LanguageReferencedException(language, ehubConsumers);
        }
        //formatAdminController.deleteFormatTextBundles(language);
        languageRepository.delete(language);
    }
}
