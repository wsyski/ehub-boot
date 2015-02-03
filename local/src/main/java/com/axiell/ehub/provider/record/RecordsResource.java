/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.security.AuthInfo;

public final class RecordsResource implements IRecordsResource {
    private final IFormatBusinessController formatBusinessController;
    private final AuthInfo authInfo;
    private final String contentProviderName;    

    public RecordsResource(final IFormatBusinessController formatBusinessController, final AuthInfo authInfo, final String contentProviderAlias) {
        this.formatBusinessController = formatBusinessController;
        this.authInfo = authInfo;
        this.contentProviderName = contentProviderAlias;
    }

    @Override
    public RecordsDTO root() {
        throw new NotImplementedException("Root path in RecordsResource has not been implemented yet");
    }

    @Override
    public RecordDTO getRecord(String contentProviderRecordId, String language) {
        Formats formats = formatBusinessController.getFormats(authInfo, contentProviderName, contentProviderRecordId, language);
        return null;
    }
}
