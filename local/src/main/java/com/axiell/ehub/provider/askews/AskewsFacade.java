package com.axiell.ehub.provider.askews;

import java.net.URL;

import org.springframework.stereotype.Component;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.IeBookService;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;

@Component
public class AskewsFacade implements IAskewsFacade {
    private static final String WSDL_LOCATION = "com/askews/api/askews.wsdl";

    private IeBookService askewsService;

    public AskewsFacade() {
        URL wsdlUrl = getClass().getClassLoader().getResource(WSDL_LOCATION);
        askewsService = new AskewsSoapService(wsdlUrl).getBasicHttpBindingIeBookService();
    }

    @Override
    public LoanRequestResult processLoan(Integer userId,
            Integer authId,
            String contentProviderRecordId,
            Integer duration,
            String tokenKey) {
        return askewsService.processLoan(userId, authId, contentProviderRecordId, duration, tokenKey);
    }

    @Override
    public ArrayOfLoanDetails getLoanDetails(Integer userId,
            Integer authId,
            Integer loanId,
            String tokenKey) {
        return askewsService.getLoanDetails(userId, authId, null, loanId, tokenKey);
    }

    @Override
    public UserLookupResult getUserID(String barcode, Integer authId, String tokenKey) {
        return askewsService.getUserID(barcode, authId, tokenKey);
    }
}
