/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

/**
 * Defines all administration methods related to {@link AdminUser}s.
 * 
 * <p>This interface should only be used by the eHUB administration interface or by other admin controllers.</p>
 */
public interface IUserAdminController {

    /**
     * Verifies that the provided {@link AdminUser} is a valid {@link AdminUser}. 
     * 
     * @param user the {@link AdminUser} to be logged in
     * @return a {@link LoginStatus}
     */
    LoginStatus login(AdminUser user);
}
