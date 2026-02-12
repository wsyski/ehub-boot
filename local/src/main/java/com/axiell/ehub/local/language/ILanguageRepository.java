/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.language;

import com.axiell.ehub.common.language.Language;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Simple CRUD Repository interface for {@link Language} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 *
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.axiell.consumer</code>. Access to this repository outside this package should be done through the
 * {@link ILanguageAdminController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the
 * Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface ILanguageRepository extends CrudRepository<Language, String> {

    /**
     * Finds all {@link Language}s in the eHUB. The returned list of {@link Language}s will be ordered by the language
     * codes in ascending order.
     *
     * @return a list of {@link Language}s
     */
    @Query("SELECT l FROM Language l ORDER BY LOWER(l.id) ASC")
    List<Language> findAllOrderedByLanguage();
}
