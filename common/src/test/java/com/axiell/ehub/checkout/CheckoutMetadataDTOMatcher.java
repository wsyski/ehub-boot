package com.axiell.ehub.checkout;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class CheckoutMetadataDTOMatcher extends BaseMatcher<CheckoutMetadataDTO> {
    private final CheckoutMetadataDTO expCheckoutMetadataDTO;

    private CheckoutMetadataDTOMatcher(final CheckoutMetadataDTO expCheckoutMetadataDTO) {
        this.expCheckoutMetadataDTO = expCheckoutMetadataDTO;
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

    @Factory
    public static CheckoutMetadataDTOMatcher matchesExpectedCheckoutMetadataDTO(CheckoutMetadataDTO expCheckoutMetadataDTO) {
        return new CheckoutMetadataDTOMatcher(expCheckoutMetadataDTO);
    }
}
