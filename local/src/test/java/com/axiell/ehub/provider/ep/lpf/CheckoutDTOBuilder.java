package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.provider.ep.FormatMetadataDTOBuilder;

import java.util.Date;

public class CheckoutDTOBuilder {
    public static final String ID = "id";

    public static CheckoutDTO defaultCheckoutDTO() {
        return new CheckoutDTO(ID, FormatMetadataDTOBuilder.defaultFormatMetadataDTO(), new Date());
    }
}
