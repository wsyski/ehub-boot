package com.axiell.ehub.util;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class HashCodeBuilderFactory {
    private static final int INITIAL_NON_ZERO_ODD_NUMVER = 17;
    private static final int MULTIPLIER_NON_ZERO_ODD_NUMBER = 31;
    
    private HashCodeBuilderFactory() {
    }
    
    public static HashCodeBuilder create() {
	return new HashCodeBuilder(INITIAL_NON_ZERO_ODD_NUMVER, MULTIPLIER_NON_ZERO_ODD_NUMBER);
    }
}
