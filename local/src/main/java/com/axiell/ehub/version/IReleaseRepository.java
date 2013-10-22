package com.axiell.ehub.version;

import org.springframework.data.repository.CrudRepository;

/**
 * Simple CRUD Repository interface for {@link Release} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 * 
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.ehub.version</code>. Access to this repository outside this package should be done through the
 * {@link IReleaseAdminController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the
 * Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IReleaseRepository extends CrudRepository<Release, String> {
}
