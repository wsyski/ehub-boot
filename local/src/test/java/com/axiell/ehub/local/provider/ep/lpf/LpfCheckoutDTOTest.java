package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.ehub.common.DTOTestFixture;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class LpfCheckoutDTOTest extends DTOTestFixture<LpfCheckoutDTO> {

    @Override
    protected LpfCheckoutDTO getTestInstance() {
        return LppCheckoutDTOBuilder.defaultCheckoutDTO();
    }

    @Override
    protected Class<LpfCheckoutDTO> getTestClass() {
        return LpfCheckoutDTO.class;
    }

    @Override
    protected void customAssertion(final LpfCheckoutDTO underTest, final LpfCheckoutDTO actualObject) {
        assertThat(actualObject.getExpirationDate(), Matchers.is(underTest.getExpirationDate()));
    }
}
