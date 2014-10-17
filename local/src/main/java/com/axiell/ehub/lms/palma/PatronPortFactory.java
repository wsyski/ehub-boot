package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.v267.patron.Patron;
import com.axiell.arena.services.palma.v267.patron.PatronWebServiceService;
import com.axiell.ehub.consumer.EhubConsumer;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
class PatronPortFactory extends AbstractPalmaPortFactory<Patron> implements IPatronPortFactory {

    @Override
    protected PalmaWsdlUrl makePalmaWsdlUrl(EhubConsumer ehubConsumer) {
        return new PalmaUrlBuilder(ehubConsumer).appendPath("/v267/patron?wsdl").build();
    }

    @Override
    protected Patron makePalmaPort(URL wsdlUrl) {
        PatronWebServiceService patronPalmaService = new PatronWebServiceService(wsdlUrl);
        return patronPalmaService.getPatron();
    }
}
