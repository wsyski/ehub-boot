package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import org.apache.commons.lang3.StringUtils;

public final class Validate {

    /**
     * Private constructor that prevents direct instantiation.
     */
    private Validate() {
    }

    public static void isNotNull(final Object underValidation, final String message) {
        if (underValidation == null) {
            throw new InternalServerErrorException(message);
        }
    }

    public static void isNotBlank(final String underValidation, final String message) {
        if (StringUtils.isBlank(underValidation)) {
            throw new InternalServerErrorException(message);
        }
    }

    public static void isNotNull(final Object underValidation, final EhubConsumer ehubConsumer, final String message) {
        String errorMessage = new StringBuilder(message).append(" ehub consumer ID = '").append(ehubConsumer.getId()).append("'").toString();
        isNotNull(underValidation, errorMessage);
    }

    public static void isNotBlank(final String underValidation, final EhubConsumer ehubConsumer, final String message) {
        String errorMessage = new StringBuilder(message).append(" ehub consumer ID = '").append(ehubConsumer.getId()).append("'").toString();
        isNotBlank(underValidation, errorMessage);
    }
}
