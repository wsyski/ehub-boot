package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.DTOTestFixture;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.util.Date;

public class CheckoutDTOTest extends DTOTestFixture<CheckoutDTO> {

    @Override
    protected CheckoutDTO getTestInstance() {
        return new CheckoutDTO("loanId", "contentUrl", new Date(1465488586000L));
    }

    @Override
    protected Class<CheckoutDTO> getTestClass() {
        return CheckoutDTO.class;
    }

    @Override
    protected void customAssertion(final CheckoutDTO underTest, final CheckoutDTO actualObject) {
        Assert.assertThat(actualObject.getExpirationDate(), Matchers.is(underTest.getExpirationDate()));
    }
}
