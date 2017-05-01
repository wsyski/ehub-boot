/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import com.axiell.auth.AuthInfo;

/**
 * Defines the business methods related to {@link EhubConsumer}s.
 * 
 * <p>
 * This interface should only be used by the resources of the eHUB REST interface or by other business controllers.
 * </p>
 */
public interface IConsumerBusinessController {
   
    EhubConsumer getEhubConsumer(Long ehubConsumerId);
   
    EhubConsumer getEhubConsumer(AuthInfo authInfo);
}
