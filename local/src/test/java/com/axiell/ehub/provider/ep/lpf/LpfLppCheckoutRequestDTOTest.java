package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.record.format.FormatBuilder;

public class LpfLppCheckoutRequestDTOTest extends DTOTestFixture<LpfCheckoutRequestDTO> {

    @Override
    protected LpfCheckoutRequestDTO getTestInstance() {
        return new LpfCheckoutRequestDTO("recordId", FormatBuilder.downloadableFormat().id());
    }

    @Override
    protected Class<LpfCheckoutRequestDTO> getTestClass() {
        return LpfCheckoutRequestDTO.class;
    }
}
