package com.axiell.ehub.local.provider.ep.lpp;

import com.axiell.ehub.common.provider.record.format.FormatBuilder;
import com.axiell.ehub.local.provider.ep.FormatMetadataDTOBuilder;

import java.util.Collections;
import java.util.Date;

public class LppCheckoutDTOBuilder {
    public static final String ID = "id";

    public static LppCheckoutDTO defaultCheckoutDTO() {
        return new LppCheckoutDTO(ID, Collections.singletonMap(FormatBuilder.downloadableFormat().getId(), FormatMetadataDTOBuilder.defaultFormatMetadataDTO()), new Date());
    }
}
