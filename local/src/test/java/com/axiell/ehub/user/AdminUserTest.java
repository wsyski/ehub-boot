/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 */
public class AdminUserTest {

    /**
     * Test method for {@link com.axiell.ehub.user.AdminUser#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        final AdminUser adminUser = new AdminUser("admin", "!gogetit1");
        assertTrue(adminUser.isValid("!gogetit1"));
        assertFalse(adminUser.isValid("1gogetit!"));
    }

}
