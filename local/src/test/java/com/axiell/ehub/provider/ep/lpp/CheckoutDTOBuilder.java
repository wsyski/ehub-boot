package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.FormatMetadataDTOBuilder;
import com.axiell.ehub.provider.record.format.FormatBuilder;

import java.util.Collections;
import java.util.Date;

public class CheckoutDTOBuilder {
    public static final String ID = "id";

    public static CheckoutDTO defaultCheckoutDTO() {
        return new CheckoutDTO(ID, Collections.singletonMap(FormatBuilder.downloadableFormat().id(), FormatMetadataDTOBuilder.defaultFormatMetadataDTO()),new Date());
    }
}
