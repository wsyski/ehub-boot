package com.axiell.ehub.consumer;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Simple CRUD Repository interface for {@link EhubConsumer} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 * 
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.ehub.consumer</code>. Access to this repository outside this package should be done through the
 * {@link IConsumerBusinessController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the
 * Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IEhubConsumerRepository extends CrudRepository<EhubConsumer, Long> {

    /**
     * Finds all {@link EhubConsumer}s in the eHUB. The returned list of {@link EhubConsumer}s will be sorted by their
     * descriptions in ascending order.
     * 
     * @return a list of {@link EhubConsumer}s
     */
    @Query("SELECT e FROM EhubConsumer e ORDER BY LOWER(e.description) ASC")
    List<EhubConsumer> findAllOrderedByDescription();
}
