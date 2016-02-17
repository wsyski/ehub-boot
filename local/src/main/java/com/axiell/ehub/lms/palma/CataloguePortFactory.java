package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.search.v267.service.Catalogue;
import com.axiell.arena.services.palma.search.v267.service.PalmaSearchService;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.logging.ISoapLoggingHandlerAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
class CataloguePortFactory extends AbstractPalmaPortFactory<Catalogue> implements ICataloguePortFactory {
    @Autowired
    private ISoapLoggingHandlerAppender soapLoggingHandlerAppender;

    @Override
    protected PalmaWsdlUrl makePalmaWsdlUrl(final EhubConsumer ehubConsumer) {
        return new PalmaUrlBuilder(ehubConsumer).appendPath("/v267/catalogue?wsdl").build();
    }

    @Override
    protected Catalogue makePalmaPort(URL wsdlURL) {
        final PalmaSearchService palmaSearchService = new PalmaSearchService(wsdlURL);
        return palmaSearchService.getCatalogue();
    }

    @Override
    protected ISoapLoggingHandlerAppender getSoapLoggingHandlerAppender() {
        return soapLoggingHandlerAppender;
    }
}
