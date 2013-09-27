package com.axiell.ehub.util;

import com.axiell.ehub.consumer.EhubConsumer;

public final class Validate {

    /**
     * Private constructor that prevents direct instantiation.
     */
    private Validate() {	
    }
    
    public static void isNotNull(Object underValidation, String message) {
	
    }    
    
    public static void isNotNull(Object underValidation, EhubConsumer ehubConsumer, String message) {
	String errorMessage = new StringBuilder(message).append(" ehub consumer ID = '").append(ehubConsumer.getId()).append("'").toString();
    }
}
