package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.arena.services.palma.search.v267.service.Catalogue;
import com.axiell.ehub.consumer.EhubConsumer;

interface ICataloguePortFactory {

    Catalogue getInstance(EhubConsumer ehubConsumer);
}
