/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.security.AuthInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public final class RecordsResource implements IRecordsResource {
    private final IFormatBusinessController formatBusinessController;
    private final String contentProviderAlias;

    public RecordsResource(final IFormatBusinessController formatBusinessController, final AuthInfo authInfo, final String contentProviderAlias) {
        this.formatBusinessController = formatBusinessController;
        this.contentProviderAlias = contentProviderAlias;
    }

    @Override
    public RecordsDTO root() {
        throw new NotImplementedException("Root path in RecordsResource has not been implemented yet");
    }

    @Override
    public RecordDTO getRecord(AuthInfo authInfo, String contentProviderRecordId, String language) {
        Formats formats = formatBusinessController.getFormats(authInfo, contentProviderAlias, contentProviderRecordId, language);
        List<FormatDTO> formatDTOs = Lists.transform(formats.asList(), new Function<Format, FormatDTO>() {
            @Override
            public FormatDTO apply(Format input) {
                return input.toDTO();
            }
        });
        return new RecordDTO().id(contentProviderRecordId).formats(formatDTOs);
    }
}
