package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.v2.provider.record.format.FormatDTO_v2;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

import static com.axiell.ehub.v2.provider.record.format.FormatDTOMatcher_v2.matchesExpectedFormatDTO;

public class CheckoutMetadataDTOMatcher_v2 extends BaseMatcher<CheckoutMetadataDTO_v2> {
    private final CheckoutMetadataDTO_v2 expCheckoutMetadataDTO;

    private CheckoutMetadataDTOMatcher_v2(CheckoutMetadataDTO_v2 expCheckoutMetadataDTO) {
        this.expCheckoutMetadataDTO = expCheckoutMetadataDTO;
    }

    @Factory
    public static CheckoutMetadataDTOMatcher_v2 matchesExpectedCheckoutMetadataDTO(CheckoutMetadataDTO_v2 expCheckoutMetadataDTO) {
        return new CheckoutMetadataDTOMatcher_v2(expCheckoutMetadataDTO);
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof CheckoutMetadataDTO_v2) {
            CheckoutMetadataDTO_v2 actCheckoutMetadataDTO = (CheckoutMetadataDTO_v2) item;
            FormatDTO_v2 actFormatDTO = actCheckoutMetadataDTO.getFormat();
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
        description.appendText("matching a CheckoutMetadataDTO_v2");
    }
}
