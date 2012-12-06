/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Simple CRUD Repository interface for {@link ContentProviderFormatTextBundle} instances. The interface is used to
 * declare so called query methods, methods to retrieve single entities or collections of them.
 * 
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.ehub.provider.record.format</code>. Access to this repository outside this package should be done through the
 * {@link IFormatBusinessController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IFormatTextBundleRepository extends CrudRepository<FormatTextBundle, Long> {
    
    /**
     * Finds all {@link FormatTextBundle}s by the provided language.
     * 
     * @param language a ISO 639 alpha-2 or alpha-3 language code
     * @return a list of {@link FormatTextBundle}s
     */
    List<FormatTextBundle> findByLanguage(String language);
}
