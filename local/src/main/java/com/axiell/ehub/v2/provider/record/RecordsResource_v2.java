/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v2.provider.record;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.v2.provider.record.format.FormatDTO_v2;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public final class RecordsResource_v2 implements IRecordsResource_v2 {
    private final IFormatBusinessController formatBusinessController;
    private final String contentProviderAlias;

    public RecordsResource_v2(final IFormatBusinessController formatBusinessController, final String contentProviderAlias) {
        this.formatBusinessController = formatBusinessController;
        this.contentProviderAlias = contentProviderAlias;
    }

    @Override
    public RecordsDTO_v2 root() {
        throw new NotImplementedException("Root path in RecordsResource has not been implemented yet");
    }

    @Override
    public RecordDTO_v2 getRecord(AuthInfo authInfo, String contentProviderRecordId, String language) {
        Formats formats = formatBusinessController.getFormats(authInfo, contentProviderAlias, contentProviderRecordId, language);
        List<FormatDTO_v2> formatDTOs = Lists.transform(formats.asList(), new FormatToFormatDTOFunction());
        return new RecordDTO_v2().id(contentProviderRecordId).formats(formatDTOs);
    }

    private static class FormatToFormatDTOFunction implements Function<Format, FormatDTO_v2> {
        @Override
        public FormatDTO_v2 apply(Format format) {
            return FormatDTO_v2.fromDTO(format.toDTO());
        }
    }
}
