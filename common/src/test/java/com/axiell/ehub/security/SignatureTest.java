/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 */
public class SignatureTest {

    /**
     * Test method for {@link com.axiell.ehub.security.Signature#toString()}.
     */
    @Test
    public void testToString() {
        Signature signature = new Signature(1L, "secret1", "909265910" , "4447");
        String actValue = signature.toString();
        assertEquals("GN%2B9mlD70ZER%2Fx3ur7w7HJRgnYU%3D", actValue);
    }

}
