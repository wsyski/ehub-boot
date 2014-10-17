package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.ehub.consumer.EhubConsumer;

interface ILoansPortFactory {

    Loans getInstance(EhubConsumer ehubConsumer);
}
