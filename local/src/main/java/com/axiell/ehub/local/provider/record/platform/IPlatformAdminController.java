/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;

import java.util.List;

/**
 * Defines all administration methods related to {@link Platform}s.
 * <p>
 * <p>This interface should only be used by the eHUB administration interface or by other admin controllers.</p>
 */
public interface IPlatformAdminController {

    /**
     * Gets a fully initialized {@link Platform}.
     *
     * @param id the ID of the {@link Platform}
     * @return a {@link Platform}
     */
    Platform getPlatform(Long id);

    /**
     * Saves the provided {@link Platform}
     *
     * @param Platform the {@link Platform} to be saved
     * @return the saved {@link Platform}
     */
    Platform save(Platform Platform);

    /**
     * Deletes the {@link Platform}.
     *
     * @param Platform the {@link Platform} to be deleted
     */
    void delete(Platform Platform);


    List<Platform> findAll();
}
