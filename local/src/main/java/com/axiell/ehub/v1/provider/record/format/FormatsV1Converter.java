package com.axiell.ehub.v1.provider.record.format;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.Formats;

public class FormatsV1Converter {

    public static Formats_v1 convert(Formats formats) {
        Formats_v1 formats_v1 = new Formats_v1();

        for (Format format : formats.getFormats()) {
            Format_v1 format_v1 = new Format_v1(format.id(), format.name(), format.description(), null);
            formats_v1.addFormat(format_v1);
        }

        return formats_v1;
    }
}
