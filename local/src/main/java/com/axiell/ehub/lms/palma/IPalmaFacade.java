package com.axiell.ehub.lms.palma;

import com.axiell.ehub.services.palma.loans.CheckOut;
import com.axiell.ehub.services.palma.loans.CheckOutResponse;
import com.axiell.ehub.services.palma.loans.CheckOutTestResponse;
import com.axiell.ehub.services.palma.loans.CheckOutTest;

interface IPalmaFacade {

    CheckOutTestResponse checkOutTest(CheckOutTest checkOutTest);

    CheckOutResponse checkOut(CheckOut checkOut);
}
