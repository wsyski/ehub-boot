package com.axiell.ehub.lms.palma;

import com.axiell.ehub.services.palma.loans.*;

class PalmaFacade implements IPalmaFacade {
    private Loans loanPort;

    PalmaFacade(final Loans loanPort) {
        this.loanPort = loanPort;
    }

    @Override
    public CheckOutTestResponse checkOutTest(final CheckOutTest checkOutTest) {
        return loanPort.checkOutTest(checkOutTest);
    }

    @Override
    public CheckOutResponse checkOut(CheckOut checkOut) {
        return loanPort.checkOut(checkOut);
    }
}
