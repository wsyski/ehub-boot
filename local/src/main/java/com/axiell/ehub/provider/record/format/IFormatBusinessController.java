/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.security.AuthInfo;

/**
 * Defines all business methods related to {@link Formats} objects.
 * 
 * <p>This interface should only be used by the resources of the eHUB REST interface or by other business controllers.</p>
 */
public interface IFormatBusinessController {

    /**
     * Gets the {@link Formats} for a specific record at a certain {@link ContentProvider}.
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID
     * @param contentProviderName the name of the {@link ContentProvider}
     * @param contentProviderRecordId the ID of the record at the {@link ContentProvider}
     * @param language the ISO 639 alpha-2 or alpha-3 language code used when getting the translated names and
     * descriptions of the {@link Formats}
     * @return an instance of {@link Formats}
     */
    Formats getFormats(AuthInfo authInfo, String contentProviderName, String contentProviderRecordId, String language);
}
