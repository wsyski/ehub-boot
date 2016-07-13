package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.FormatMetadataDTOBuilder;
import com.axiell.ehub.provider.record.format.FormatBuilder;

import java.util.Collections;
import java.util.Date;

public class LppCheckoutDTOBuilder {
    public static final String ID = "id";

    public static LppCheckoutDTO defaultCheckoutDTO() {
        return new LppCheckoutDTO(ID, Collections.singletonMap(FormatBuilder.downloadableFormat().getId(), FormatMetadataDTOBuilder.defaultFormatMetadataDTO()),new Date());
    }
}
