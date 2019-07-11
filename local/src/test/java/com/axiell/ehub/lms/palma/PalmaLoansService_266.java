package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.arena.services.palma.patron.checkoutrequest.CheckOutRequest;
import com.axiell.arena.services.palma.patron.checkouttestrequest.CheckOutTestRequest;
import com.axiell.arena.services.palma.patron.loansrequest.LoansRequest;
import com.axiell.arena.services.palma.patron.renewloansrequest.RenewLoansRequest;
import com.axiell.ehub.DevelopmentData;
import org.junit.Assert;

import javax.jws.WebService;

@WebService(serviceName = "LoansPalmaService", portName = "loans", targetNamespace = "http://loans.palma.services.arena.axiell.com/",
        wsdlLocation = "com/axiell/ehub/lms/palma_266/loans.wsdl", endpointInterface = "com.axiell.arena.services.palma.loans.Loans")
public class PalmaLoansService_266 extends AbstractPalmaService implements Loans {
    private static final String CONTEXT_PATH = "com.axiell.arena.services.palma.loans";

    @Override
    public com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse checkOutTest(final CheckOutTestRequest parameters) {
        if (parameters.getUser().equals(DevelopmentData.LIBRARY_CARD)) {
            verifyCheckOutTestOk(parameters);
            return ((com.axiell.arena.services.palma.loans.CheckOutTestResponse) getFileResponseUnmarshaller().unmarshalFromFile(PALMA_CHECK_OUT_TEST_RESPONSE_OK_XML)).getCheckOutTestResponse();
        } else {
            verifyCheckOutTestError(parameters);
            return ((com.axiell.arena.services.palma.loans.CheckOutTestResponse) getFileResponseUnmarshaller().unmarshalFromFile(PALMA_CHECK_OUT_TEST_RESPONSE_ERROR_XML)).getCheckOutTestResponse();
        }
    }

    @Override
    public com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse checkOut(final CheckOutRequest parameters) {
        verifyCheckOut(parameters);
        return ((com.axiell.arena.services.palma.loans.CheckOutResponse) getFileResponseUnmarshaller().unmarshalFromFile(PALMA_CHECK_OUT_RESPONSE_XML))
                .getCheckOutResponse();
    }

    @Override
    public com.axiell.arena.services.palma.patron.renewloansresponse.RenewLoansResponse renewLoans(final RenewLoansRequest parameters) {
        return null;
    }

    @Override
    public com.axiell.arena.services.palma.patron.loansresponse.LoansResponse getLoans(final LoansRequest parameters) {
        return null;
    }

    @Override
    protected String getContextPath() {
        return CONTEXT_PATH;
    }

    protected void verifyCheckOutTestOk(final CheckOutTestRequest checkOutTestRequest) {
        Assert.assertNotNull(checkOutTestRequest);
        Assert.assertEquals(checkOutTestRequest.getArenaMember(), DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        Assert.assertEquals(checkOutTestRequest.getRecordId(), DevelopmentData.LMS_RECORD_ID);
        Assert.assertEquals(checkOutTestRequest.getUser(), DevelopmentData.LIBRARY_CARD);
        Assert.assertEquals(checkOutTestRequest.getPassword(), DevelopmentData.PIN);
        Assert.assertEquals(checkOutTestRequest.getContentProviderFormatId(), DevelopmentData.TEST_EP_FORMAT_0_ID);
        Assert.assertEquals(checkOutTestRequest.getContentProviderName(), DevelopmentData.CONTENT_PROVIDER_TEST_EP);
    }

    protected void verifyCheckOutTestError(final CheckOutTestRequest checkOutTestRequest) {
        Assert.assertNotNull(checkOutTestRequest);
        Assert.assertEquals(checkOutTestRequest.getArenaMember(), DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        Assert.assertEquals(checkOutTestRequest.getRecordId(), DevelopmentData.LMS_RECORD_ID);
        Assert.assertEquals(checkOutTestRequest.getUser(), BLOCKED_LIBRARY_CARD);
        Assert.assertEquals(checkOutTestRequest.getPassword(), DevelopmentData.PIN);
        Assert.assertEquals(checkOutTestRequest.getContentProviderFormatId(), DevelopmentData.TEST_EP_FORMAT_0_ID);
        Assert.assertEquals(checkOutTestRequest.getContentProviderName(), DevelopmentData.CONTENT_PROVIDER_TEST_EP);
    }

    protected void verifyCheckOut(final CheckOutRequest checkOutRequest) {
        Assert.assertNotNull(checkOutRequest);
        Assert.assertEquals(checkOutRequest.getArenaMember(), DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        Assert.assertEquals(checkOutRequest.getRecordId(), DevelopmentData.LMS_RECORD_ID);
        Assert.assertEquals(checkOutRequest.getUser(), DevelopmentData.LIBRARY_CARD);
        Assert.assertEquals(checkOutRequest.getPassword(), DevelopmentData.PIN);
        Assert.assertEquals(checkOutRequest.getContentProviderFormatId(), DevelopmentData.TEST_EP_FORMAT_0_ID);
        Assert.assertEquals(checkOutRequest.getContentProviderName(), DevelopmentData.CONTENT_PROVIDER_TEST_EP);
    }
}