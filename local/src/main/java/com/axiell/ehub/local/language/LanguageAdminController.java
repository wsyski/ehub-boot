/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.language;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.local.consumer.IEhubConsumerRepository;
import com.axiell.ehub.local.error.IErrorCauseArgumentValueAdminController;
import com.axiell.ehub.local.provider.record.format.IFormatAdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class LanguageAdminController implements ILanguageAdminController {
    @Autowired
    private ILanguageRepository languageRepository;

    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IEhubConsumerRepository ehubConsumerRepository;

    @Autowired
    private IErrorCauseArgumentValueAdminController errorCauseArgumentValueAdminController;

    /**
     * @see ILanguageAdminController#getLanguages()
     */
    @Override
    @Transactional(readOnly = true)
    public List<Language> getLanguages() {
        return languageRepository.findAllOrderedByLanguage();
    }

    /**
     * @see ILanguageAdminController#save(Language)
     */
    @Override
    @Transactional(readOnly = false)
    public Language save(Language language) {
        return languageRepository.save(language);
    }

    /**
     * @see ILanguageAdminController#delete(Language)
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(final Language language) throws LanguageReferencedException {
        List<EhubConsumer> ehubConsumers = ehubConsumerRepository.findByDefaultLanguage(language);
        if (!ehubConsumers.isEmpty()) {
            throw new LanguageReferencedException(language, ehubConsumers);
        }
        formatAdminController.deleteFormatTextBundles(language);
        errorCauseArgumentValueAdminController.deleteErrorCauseArgumentValueTextBundles(language);
        languageRepository.delete(language);
    }
}
