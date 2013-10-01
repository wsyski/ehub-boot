package com.axiell.ehub.lms.palma;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.springframework.stereotype.Component;

import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.arena.services.palma.loans.LoansPalmaService;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.logging.LoggingHandler;
import com.axiell.ehub.util.Validate;

@Component
public class PalmaFacadeFactory implements IPalmaFacadeFactory {

    private volatile ConcurrentMap<String, IPalmaFacade> palmaFacades = new ConcurrentHashMap<>();

    @Override
    public IPalmaFacade getInstance(final EhubConsumer ehubConsumer) {
        String loansServiceWsdlUrl = getLoansServiceWsdlUrl(ehubConsumer);
        IPalmaFacade palmaFacade = palmaFacades.get(loansServiceWsdlUrl);
        if (palmaFacade == null) {
            Loans loanPort = getLoanPort(loansServiceWsdlUrl);
            addLoggingHandler(loanPort);
            palmaFacade = new PalmaFacade(loanPort);
            palmaFacades.put(loansServiceWsdlUrl, palmaFacade);
        }
        return palmaFacade;
    }

    private Loans getLoanPort(final String loansServiceWsdlUrl) {
        URL loansServiceWsdlURL = getLoansServiceWsdlURL(loansServiceWsdlUrl);
        LoansPalmaService loansPalmaService = new LoansPalmaService(loansServiceWsdlURL);
        return loansPalmaService.getLoans();
    }

    private void addLoggingHandler(final Loans loanPort) {
        BindingProvider bp = (BindingProvider) loanPort;
        Binding binding = bp.getBinding();
        @SuppressWarnings("rawtypes")
	List<Handler> handlerList = binding.getHandlerChain();
        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }
        LoggingHandler loggingHandler = new LoggingHandler();
        handlerList.add(loggingHandler);
        binding.setHandlerChain(handlerList);
    }

    private URL getLoansServiceWsdlURL(final String loansServiceWsdlUrl) {
        try {
            return new URL(loansServiceWsdlUrl);
        } catch (MalformedURLException ex) {
            throw new InternalServerErrorException("Invalid loans service wsdl url: " + loansServiceWsdlUrl, ex);
        }
    }

    private String getLoansServiceWsdlUrl(final EhubConsumer ehubConsumer) {
        String arenaPalmaUrl = getArenaPalmaUrl(ehubConsumer);
        return arenaPalmaUrl + "/loans?wsdl";
    }

    private String getArenaPalmaUrl(final EhubConsumer ehubConsumer) {
        EhubConsumer.EhubConsumerPropertyKey arenaPalmaUrlKey = EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL;
        String arenaPalmaUrl = ehubConsumer.getProperties().get(arenaPalmaUrlKey);
        Validate.isNotBlank(arenaPalmaUrl, ehubConsumer, "Arena palma url was blank");
        return arenaPalmaUrl;
    }
}
