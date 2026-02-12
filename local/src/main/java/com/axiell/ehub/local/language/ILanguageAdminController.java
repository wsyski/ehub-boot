/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.language;

import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.record.format.FormatTextBundle;

import java.util.List;

/**
 * Defines all administration methods related to {@link Language}s.
 *
 * <p>This interface should only be used by the eHUB administration interface or by other admin controllers.</p>
 */
public interface ILanguageAdminController {

    /**
     * Gets a list of all available {@link Language}s in the eHUB.
     *
     * @return a list of {@link Language}s
     */
    List<Language> getLanguages();

    /**
     * Saves the provided {@link Language}.
     *
     * @param language the {@link Language} to be saved
     * @return the saved {@link Language}
     */
    Language save(Language language);

    /**
     * Deletes the provided {@link Language} and all related {@link FormatTextBundle}s.
     *
     * @param language the {@link Language} to be deleted
     */
    void delete(Language language) throws LanguageReferencedException;
}
