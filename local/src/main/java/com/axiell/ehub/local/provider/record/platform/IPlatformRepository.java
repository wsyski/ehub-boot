package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Simple CRUD Repository interface for {@link Platform} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 * <p>
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.axiell.provider.record.platform</code>.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IPlatformRepository extends CrudRepository<Platform, Long> {

    @Query("SELECT p FROM Platform p ORDER BY LOWER(p.name) ASC")
    List<Platform> findAllOrderedByName();
}
