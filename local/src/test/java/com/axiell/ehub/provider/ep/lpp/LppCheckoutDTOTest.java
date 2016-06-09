package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.DTOTestFixture;
import org.hamcrest.Matchers;
import org.junit.Assert;

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
        Assert.assertThat(actualObject.getExpirationDate(), Matchers.is(underTest.getExpirationDate()));
    }
}
