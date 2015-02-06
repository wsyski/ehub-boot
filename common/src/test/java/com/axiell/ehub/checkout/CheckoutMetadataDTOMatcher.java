package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.FormatDTO;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

import static com.axiell.ehub.provider.record.format.FormatDTOMatcher.matchesExpectedFormatDTO;

public class CheckoutMetadataDTOMatcher extends BaseMatcher<CheckoutMetadataDTO> {
    private final CheckoutMetadataDTO expCheckoutMetadataDTO;

    private CheckoutMetadataDTOMatcher(CheckoutMetadataDTO expCheckoutMetadataDTO) {
        this.expCheckoutMetadataDTO = expCheckoutMetadataDTO;
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof CheckoutMetadataDTO) {
            CheckoutMetadataDTO actCheckoutMetadataDTO = (CheckoutMetadataDTO) item;
            FormatDTO actFormatDTO = actCheckoutMetadataDTO.getFormat();
            return matchesExpectedFormatDTO(expCheckoutMetadataDTO.getFormat()).matches(actFormatDTO)
                    && expCheckoutMetadataDTO.getId().equals(actCheckoutMetadataDTO.getId()) &&
                    expCheckoutMetadataDTO.getLmsLoanId().equals(actCheckoutMetadataDTO.getLmsLoanId()) &&
                    expCheckoutMetadataDTO.getContentProviderLoanId().equals(actCheckoutMetadataDTO.getContentProviderLoanId()) &&
                    expCheckoutMetadataDTO.getExpirationDate().equals(actCheckoutMetadataDTO.getExpirationDate());
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
