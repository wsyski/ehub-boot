package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class Record implements Serializable {
    private final RecordDTO recordDTO;
    private final List<Format> formats;

    public Record(RecordDTO recordDTO) {
        this.recordDTO = recordDTO;
        formats = Lists.transform(recordDTO.getFormats(), new FormatDTOToFormatFunction());
    }

    public String id() {
        return recordDTO.getId();
    }

    public List<Format> formats() {
        return formats;
    }

    public RecordDTO toDTO() {
        return recordDTO;
    }


    private static class FormatDTOToFormatFunction implements Function<FormatDTO, Format>, Serializable {
        @Override
        public Format apply(FormatDTO formatDTO) {
            return new Format(formatDTO);
        }
    }
}
