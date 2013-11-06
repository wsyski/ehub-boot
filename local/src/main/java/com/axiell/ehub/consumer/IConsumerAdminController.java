/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import java.util.List;

/**
 * Defines all administration methods related to {@link EhubConsumer}s and {@link ContentProviderConsumer}s.
 * 
 * <p>This interface should only be used by the eHUB administration interface or by other admin controllers.</p>
 */
public interface IConsumerAdminController {
    /**
     * Gets a fully initialized {@link EhubConsumer}.
     * 
     * @param ehubConsumerId the ID of the {@link EhubConsumer}
     * @return an {@link EhubConsumer}
     */
    EhubConsumer getEhubConsumer(Long ehubConsumerId);

    /**
     * Gets a list of {@link EhubConsumer}s sorted by their descriptions in ascending order.
     * 
     * <p>
     * <b>NOTE:</b> The {@link EhubConsumer}s might not have been fully initialized.
     * </p>
     * 
     * @return a list of {@link EhubConsumer}s
     */
    List<EhubConsumer> getEhubConsumers();

    /**
     * Saves the provided {@link EhubConsumer}, i.e. creates a new {@link EhubConsumer} or updates the provided
     * {@link EhubConsumer}.
     * 
     * @param ehubConsumer the {@link EhubConsumer} to be saved
     * @return the saved {@link EhubConsumer}
     */
    EhubConsumer save(EhubConsumer ehubConsumer);
    
    /**
     * Deletes the provided {@link EhubConsumer} and all its {@link ContentProviderConsumer}s.
     * 
     * @param ehubConsumer the {@link EhubConsumer} to delete
     */
    void delete(EhubConsumer ehubConsumer);
    
    /**
     * Saves the provided {@link ContentProviderConsumer}.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be saved
     * @return the saved {@link ContentProviderConsumer}
     */
    ContentProviderConsumer save(ContentProviderConsumer contentProviderConsumer);
    
    /**
     * Deletes the provided {@link ContentProviderConsumer}.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to delete
     */
    void delete(ContentProviderConsumer contentProviderConsumer);

    /**
     * 
     * @param ehubConsumerId
     * @param contentProviderConsumer
     * @return
     */
    ContentProviderConsumer add(Long ehubConsumerId, ContentProviderConsumer contentProviderConsumer);
}
