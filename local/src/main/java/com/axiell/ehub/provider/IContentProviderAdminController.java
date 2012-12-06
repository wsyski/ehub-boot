/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import java.util.List;

/**
 * Defines all administration methods related to {@link ContentProvider}s.
 * 
 * <p>This interface should only be used by the eHUB administration interface or by other admin controllers.</p>
 */
public interface IContentProviderAdminController {

    /**
     * Gets a list of {@link ContentProvider}s sorted by their descriptions in ascending order.
     * 
     * <p>
     * <b>NOTE:</b> The {@link ContentProvider}s might not have been fully initialized.
     * </p>
     * 
     * @return a list of {@link ContentProvider}s
     */
    List<ContentProvider> getContentProviders();

    /**
     * Gets a fully initialized {@link ContentProvider}.
     * 
     * @param contentProviderName the name of the {@link ContentProvider} to get
     * @return a {@link ContentProvider}
     */
    ContentProvider getContentProvider(ContentProviderName contentProviderName);

    /**
     * Saves the provided {@link ContentProvider}.
     * 
     * @param contentProvider the {@link ContentProvider} to be saved
     * @return the saved {@link ContentProvider}
     */
    ContentProvider save(ContentProvider contentProvider);
}
