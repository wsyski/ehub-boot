package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.DTOTestFixture;
import org.hamcrest.Matchers;
import org.junit.Assert;

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
        Assert.assertThat(actualObject.getExpirationDate(), Matchers.is(underTest.getExpirationDate()));
    }
}
