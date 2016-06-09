package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.DTOTestFixture;

import java.util.Date;

public class CheckoutDTOTest extends DTOTestFixture<CheckoutDTO> {

    @Override
    protected CheckoutDTO getTestInstance() {
        return new CheckoutDTO("loanId","contentUrl",new Date());
    }

    @Override
    protected Class<CheckoutDTO> getTestClass() {
        return CheckoutDTO.class;
    }
}
