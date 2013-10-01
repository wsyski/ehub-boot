package com.axiell.ehub.loan;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.util.XjcSupport;

public class PendingLoanTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PendingLoanTest.class);    
    private String expXml;
    private PendingLoan expPendingLoan;
    private PendingLoan actPendingLoan;
    
    @Before
    public void setUp() {
        expPendingLoan = new PendingLoan("lmsRecordId", ContentProviderName.ELIB.toString(), "9100128260", "58");
    }    

    @Test
    public void unmarshalPendingLoanXml() throws JAXBException {
	givenPendingLoanAsXml();
	whenUnmarshalPeningLoanXml();
	thenActualPendingLoanEqualsExpectedPendingLoan();        
    }
    
    private void givenPendingLoanAsXml() {
	expXml = XjcSupport.marshal(expPendingLoan);
	LOGGER.debug(expXml);
    }
    
    private void whenUnmarshalPeningLoanXml() throws JAXBException {
	actPendingLoan = (PendingLoan) XjcSupport.unmarshal(expXml);
    }
    
    private void thenActualPendingLoanEqualsExpectedPendingLoan() {
	Assert.assertEquals(expPendingLoan.getLmsRecordId(), actPendingLoan.getLmsRecordId());
        Assert.assertEquals(expPendingLoan.getContentProviderName(), actPendingLoan.getContentProviderName());
        Assert.assertEquals(expPendingLoan.getContentProviderRecordId(), actPendingLoan.getContentProviderRecordId());
        Assert.assertEquals(expPendingLoan.getContentProviderFormatId(), actPendingLoan.getContentProviderFormatId());
    }
}
