/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record;

import java.util.Locale;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.security.AuthInfo;

/**
 * Default implementation of the {@link IRecordsResource}.
 */
public final class RecordsResource implements IRecordsResource {
    private final IFormatBusinessController formatBusinessController;
    private final String contentProviderName;    
    
    /**
     * Constructs a new {@link RecordsResource}.
     * 
     * @param formatBusinessController the {@link IFormatBusinessController}
     * @param contentProviderName the name of the {@link ContentProvider}
     */
    public RecordsResource(final IFormatBusinessController formatBusinessController, final String contentProviderName) {
        this.formatBusinessController = formatBusinessController;
        this.contentProviderName = contentProviderName;
    }
    
    /**
     * @see com.axiell.ehub.provider.record.IRecordsResource#getFormats(com.axiell.ehub.security.AuthInfo, java.lang.String, java.lang.String)
     */
    @Override
    public Formats getFormats(AuthInfo authInfo, String recordId, String language) {
        if (language == null) {
            language = Locale.ENGLISH.getLanguage();
        }
        return formatBusinessController.getFormats(authInfo, contentProviderName, recordId, language);
    }
}
