package com.axiell.ehub.local.provider.ep.lpp;

import com.axiell.ehub.common.DTOTestFixture;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class LppCheckoutDTOTest extends DTOTestFixture<LppCheckoutDTO> {

    @Override
    protected LppCheckoutDTO getTestInstance() {
        return LppCheckoutDTOBuilder.defaultCheckoutDTO();
    }

    @Override
    protected Class<LppCheckoutDTO> getTestClass() {
        return LppCheckoutDTO.class;
    }

    @Override
    protected void customAssertion(final LppCheckoutDTO underTest, final LppCheckoutDTO actualObject) {
        assertThat(actualObject.getExpirationDate(), Matchers.is(underTest.getExpirationDate()));
    }
}
