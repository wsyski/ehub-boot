package com.axiell.ehub.checkout;

import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class CheckoutMetadataDTOMatcher extends BaseMatcher<CheckoutMetadataDTO> {
    private final CheckoutMetadataDTO expCheckoutMetadataDTO;

    private CheckoutMetadataDTOMatcher(final CheckoutMetadataDTO expCheckoutMetadataDTO) {
        this.expCheckoutMetadataDTO = expCheckoutMetadataDTO;
    }

    public static CheckoutMetadataDTOMatcher matchesExpectedCheckoutMetadataDTO(CheckoutMetadataDTO expCheckoutMetadataDTO) {
        return new CheckoutMetadataDTOMatcher(expCheckoutMetadataDTO);
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof CheckoutMetadataDTO) {
            CheckoutMetadataDTO actCheckoutMetadataDTO = (CheckoutMetadataDTO) item;
            return expCheckoutMetadataDTO.equals(actCheckoutMetadataDTO);
        } else
            return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matching a CheckoutMetadataDTO");
    }
}
