/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.language.Language;

/**
 * Defines all administration methods related to {@link FormatDecoration}s and {@link FormatTextBundle}s.
 * 
 * <p>This interface should only be used by the eHUB administration interface or by other admin controllers.</p>
 */
public interface IFormatAdminController {

    /**
     * Gets a fully initialized {@link FormatDecoration}.
     * 
     * @param formatDecorationId the ID of the {@link FormatDecoration}
     * @return a {@link FormatDecoration}
     */
    FormatDecoration getFormatDecoration(Long formatDecorationId);

    /**
     * Saves the provided {@link FormatDecoration}
     * 
     * @param formatDecoration the {@link FormatDecoration} to be saved
     * @return the saved {@link FormatDecoration}
     */
    FormatDecoration save(FormatDecoration formatDecoration);

    /**
     * Deletes the {@link FormatDecoration} and all its {@link FormatTextBundle}s.
     * 
     * @param formatDecoration the {@link FormatDecoration} to be deleted
     */
    void delete(FormatDecoration formatDecoration);

    /**
     * Saves the provided {@link FormatTextBundle}.
     * 
     * @param formatTextBundle the {@link FormatTextBundle} to be saved
     * @return the saved {@link FormatTextBundle}
     */
    FormatTextBundle save(FormatTextBundle formatTextBundle);

    /**
     * Deletes the {@link FormatTextBundle}.
     * 
     * @param formatTextBundle the {@link FormatTextBundle} to be deleted
     */
    void delete(FormatTextBundle formatTextBundle);

    /**
     * Deletes all {@link FormatTextBundle}s for the provided {@link Language}.
     * 
     * @param language a {@link Language}
     */
    void deleteFormatTextBundles(Language language);
}
