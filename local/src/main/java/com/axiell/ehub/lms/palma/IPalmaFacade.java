package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.CheckOut;
import com.axiell.arena.services.palma.loans.CheckOutResponse;
import com.axiell.arena.services.palma.loans.CheckOutTest;
import com.axiell.arena.services.palma.loans.CheckOutTestResponse;

interface IPalmaFacade {

    CheckOutTestResponse checkOutTest(CheckOutTest checkOutTest);

    CheckOutResponse checkOut(CheckOut checkOut);
}
