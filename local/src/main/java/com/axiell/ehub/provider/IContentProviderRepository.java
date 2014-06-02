package com.axiell.ehub.provider;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Simple CRUD Repository interface for {@link com.axiell.ehub.contentprovider.ContentProvider} instances. The interface
 * is used to declare so called query methods, methods to retrieve single entities or collections of them.
 * 
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.axiell.provider</code>. Access to this repository outside this package should be done through the
 * {@link IContentProviderBusinessController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the
 * Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IContentProviderRepository extends CrudRepository<ContentProvider, Long> {

    /**
     * Finds a {@link ContentProvider} by its name.
     * 
     * @param name the name of the {@link ContentProvider}
     * @return a {@link ContentProvider}
     */
    ContentProvider findByName(ContentProviderName name);

    /**
     * Finds all available {@link ContentProvider}s in the eHUB. The returned list of {@link ContentProvider}s will be
     * ordered by their names.
     * 
     * @return a list of {@link ContentProvider}s
     */
    @Query("SELECT c FROM ContentProvider c ORDER BY LOWER(c.name) ASC")
    List<ContentProvider> findAllOrderedByName();
}
