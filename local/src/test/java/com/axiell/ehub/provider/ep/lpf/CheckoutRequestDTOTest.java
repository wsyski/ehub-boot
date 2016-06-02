package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.record.format.FormatBuilder;

public class CheckoutRequestDTOTest extends DTOTestFixture<CheckoutRequestDTO> {

    @Override
    protected CheckoutRequestDTO getTestInstance() {
        return new CheckoutRequestDTO("recordId", FormatBuilder.downloadableFormat().id());
    }

    @Override
    protected Class<CheckoutRequestDTO> getTestClass() {
        return CheckoutRequestDTO.class;
    }
}
