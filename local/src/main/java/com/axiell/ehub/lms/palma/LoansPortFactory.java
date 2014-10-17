package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.arena.services.palma.loans.LoansPalmaService;
import com.axiell.ehub.consumer.EhubConsumer;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
class LoansPortFactory extends AbstractPalmaPortFactory<Loans> implements ILoansPortFactory {

    @Override
    protected PalmaWsdlUrl makePalmaWsdlUrl(final EhubConsumer ehubConsumer) {
        return new PalmaUrlBuilder(ehubConsumer).appendPath("/loans?wsdl").build();
    }

    @Override
    protected Loans makePalmaPort(URL wsdlURL) {
        final LoansPalmaService loansPalmaService = new LoansPalmaService(wsdlURL);
        return loansPalmaService.getLoans();
    }
}
