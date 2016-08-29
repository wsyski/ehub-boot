/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link IUserAdminController}.
 */
public class UserAdminController implements IUserAdminController {
    @Autowired
    private IAdminUserRepository adminUserRepository;

    /**
     * @see com.axiell.ehub.user.IUserAdminController#login(com.axiell.ehub.user.AdminUser)
     */
    @Override
    @Transactional(readOnly = true)
    public LoginStatus login(final AdminUser providedUser) {
        final String name = providedUser.getName();
        final String clearPassword = providedUser.getClearPassword();
        final AdminUser retrievedUser = adminUserRepository.findOneByName(name);
        
        if (retrievedUser == null) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (retrievedUser.isValid(clearPassword)) {
            return LoginStatus.SUCCESS;
        } else {
            return LoginStatus.INVALID_PASSWORD;
        }
    }
}
