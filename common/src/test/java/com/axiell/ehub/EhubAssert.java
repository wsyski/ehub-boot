package com.axiell.ehub;


import org.junit.jupiter.api.Assertions;

public class EhubAssert {

    /**
     * Private constructor that prevents direct instantiation.
     */
    private EhubAssert() {
    }

    public static void thenInternalServerErrorExceptionIsThrown(InternalServerErrorException e) {
        Assertions.assertNotNull(e);
    }

    public static void thenNotFoundExceptionIsThrown(NotFoundException e) {
        Assertions.assertNotNull(e);
    }
}
