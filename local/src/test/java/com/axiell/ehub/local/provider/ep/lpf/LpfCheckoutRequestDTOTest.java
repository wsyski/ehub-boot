package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.ehub.common.DTOTestFixture;
import com.axiell.ehub.common.provider.record.format.FormatBuilder;

public class LpfCheckoutRequestDTOTest extends DTOTestFixture<LpfCheckoutRequestDTO> {

    @Override
    protected LpfCheckoutRequestDTO getTestInstance() {
        return new LpfCheckoutRequestDTO("recordId", FormatBuilder.downloadableFormat().getId());
    }

    @Override
    protected Class<LpfCheckoutRequestDTO> getTestClass() {
        return LpfCheckoutRequestDTO.class;
    }
}
