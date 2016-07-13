package com.axiell.ehub.v2.provider.record;

import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.v1.provider.record.format.Format_v1;
import com.axiell.ehub.v1.provider.record.format.Formats_v1;
import com.axiell.ehub.v2.provider.record.format.FormatDTOV2Converter;

import java.util.ArrayList;
import java.util.List;

public class RecordDTOV2Converter {

    private RecordDTOV2Converter() {
    }

    public static RecordDTO_v2 convert(final Record record) {
        return new RecordDTO_v2().id(record.getId()).formats(FormatDTOV2Converter.convert(record.getIssues()));
    }
}
