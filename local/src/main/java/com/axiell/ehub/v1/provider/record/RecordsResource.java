/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.provider.record;

import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.v1.provider.record.format.FormatsV1Converter;
import com.axiell.ehub.v1.provider.record.format.Formats_v1;

import java.util.Locale;

public final class RecordsResource implements IRecordsResource {
    private static final String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();
    private final IFormatBusinessController formatBusinessController;
    private final String contentProviderName;

    public RecordsResource(final IFormatBusinessController formatBusinessController, final String contentProviderName) {
        this.formatBusinessController = formatBusinessController;
        this.contentProviderName = contentProviderName;
    }

    @Override
    public Formats_v1 getFormats(AuthInfo authInfo, String recordId, String language) {
        Formats formats;
        if (language == null)
            formats = formatBusinessController.getFormats(authInfo, contentProviderName, recordId, DEFAULT_LANGUAGE);
        formats = formatBusinessController.getFormats(authInfo, contentProviderName, recordId, language);
        return FormatsV1Converter.convert(formats);
    }
}
