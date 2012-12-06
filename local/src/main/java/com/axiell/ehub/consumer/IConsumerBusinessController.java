/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

/**
 * Defines the business methods related to {@link EhubConsumer}s.
 * 
 * <p>
 * This interface should only be used by the resources of the eHUB REST interface or by other business controllers.
 * </p>
 */
public interface IConsumerBusinessController {

    /**
     * Gets the {@link EhubConsumer} with the given ID.
     * 
     * @param ehubConsumerId the ID of the {@link EhubConsumer}
     * @return an {@link EhubConsumer}, <code>null</code> if no {@link EhubConsumer} is found for the given ID
     */
    EhubConsumer getEhubConsumer(Long ehubConsumerId);
}
