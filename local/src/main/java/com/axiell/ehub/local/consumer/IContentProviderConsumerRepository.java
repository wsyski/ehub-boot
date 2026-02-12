package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Simple CRUD Repository interface for {@link ContentProviderConsumer} instances. The interface is used to declare so called query methods,
 * methods to retrieve single entities or collections of them.
 *
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.axiell.consumer</code>. Access to this repository outside this package should be done through the
 * {@link IConsumerBusinessController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IContentProviderConsumerRepository extends CrudRepository<ContentProviderConsumer, Long> {
    @Modifying
    @Query("DELETE FROM ContentProviderConsumer c WHERE c.contentProvider.id = :contentProviderId")
    void deleteByContentProviderId(@Param("contentProviderId") long contentProviderId);
}
