package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public class Record {
    private final RecordDTO recordDTO;
    private final List<Format> formats;

    public Record(RecordDTO recordDTO) {
        this.recordDTO = recordDTO;
        formats = Lists.transform(recordDTO.getFormats(), new Function<FormatDTO, Format>() {
            @Override
            public Format apply(FormatDTO input) {
                return new Format(input);
            }
        });
    }

    public String id() {
        return recordDTO.getId();
    }

    public List<Format> formats() {
        return formats;
    }
}
