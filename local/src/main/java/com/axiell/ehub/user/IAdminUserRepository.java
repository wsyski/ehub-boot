/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

import org.springframework.data.repository.CrudRepository;

/**
 * Simple CRUD Repository interface for {@link AdminUser} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 * 
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.ehub.consumer</code>. Access to this repository outside this package should be done through the
 * {@link IUserAdminController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the
 * Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IAdminUserRepository extends CrudRepository<AdminUser, Long> {

    /**
     * Finds the {@link AdminUser} by name.
     * 
     * @param name the name of the {@link AdminUser}
     * @return an {@link AdminUser}
     */
    AdminUser findByName(String name);
}
