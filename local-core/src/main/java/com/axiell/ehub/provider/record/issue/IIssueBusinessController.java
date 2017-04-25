package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.auth.AuthInfo;

import java.util.List;

/**
 * Defines all business methods related to {@link List<Format>} objects.
 * 
 * <p>This interface should only be used by the resources of the eHUB REST interface or by other business controllers.</p>
 */
public interface IIssueBusinessController {

    /**
     * Gets the {@link List<Format>} for a specific record at a certain {@link ContentProvider}.
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID
     * @param contentProviderName the name of the {@link ContentProvider}
     * @param contentProviderRecordId the ID of the record at the {@link ContentProvider}
     * @param language the ISO 639 alpha-2 or alpha-3 language code used when getting the translated names and
     * descriptions of the {@link List<Issue>}
     * @return an instance of {@link List<Issue>}
     */
    List<Issue> getIssues(AuthInfo authInfo, String contentProviderName, String contentProviderRecordId, String language);
}
