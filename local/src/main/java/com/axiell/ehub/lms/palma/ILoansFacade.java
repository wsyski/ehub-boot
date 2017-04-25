package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.auth.Patron;

import java.util.Date;

interface ILoansFacade {

    CheckOutTestResponse checkOutTest(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Patron patron, boolean isLoanPerProduct);

    CheckOutResponse checkOut(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Date expirationDate, Patron patron, boolean isLoanPerProduct);
}
