package com.axiell.ehub;


import org.junit.Assert;

public class EhubAssert {

    /**
     * Private constructor that prevents direct instantiation.
     */
    private EhubAssert() {
    }

    public static void thenInternalServerErrorExceptionIsThrown(InternalServerErrorException e) {
        Assert.assertNotNull(e);
    }

    public static void thenNotFoundExceptionIsThrown(NotFoundException e) {
        Assert.assertNotNull(e);
    }
}
