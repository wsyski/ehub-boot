package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.CheckOut;
import com.axiell.arena.services.palma.loans.CheckOutResponse;
import com.axiell.arena.services.palma.loans.CheckOutTestResponse;
import com.axiell.arena.services.palma.loans.CheckOutTest;

interface IPalmaFacade {

    CheckOutTestResponse checkOutTest(CheckOutTest checkOutTest);

    CheckOutResponse checkOut(CheckOut checkOut);
}
