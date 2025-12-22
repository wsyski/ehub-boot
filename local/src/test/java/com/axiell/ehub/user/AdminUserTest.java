/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertTrue(adminUser.isValid("!gogetit1"));
        Assertions.assertFalse(adminUser.isValid("1gogetit!"));
    }

}
