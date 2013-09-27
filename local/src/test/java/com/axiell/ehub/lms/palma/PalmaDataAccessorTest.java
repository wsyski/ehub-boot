package com.axiell.ehub.lms.palma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.Endpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.arena.services.palma.loans.CheckOut;
import com.axiell.arena.services.palma.loans.CheckOutTest;
import com.axiell.arena.services.palma.loans.GetLoans;
import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.arena.services.palma.loans.RenewLoans;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.ContentProviderName;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/axiell/ehub/common-context.xml")
public class PalmaDataAccessorTest {
    private Endpoint endpoint;

    @Autowired
    private PalmaDataAccessor palmaDataAccessor;

    private EhubConsumer ehubConsumer;
    private PendingLoan pendingLoan;

    @Before
    public void setUp() throws Exception {
        palmaDataAccessor = new PalmaDataAccessor();
        palmaDataAccessor.setPalmaFacadeFactory(new PalmaFacadeFactory());
        ehubConsumer = DevelopmentData.createEhubConsumer();
        ReflectionTestUtils.setField(ehubConsumer, "id", 1L);
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), DevelopmentData.ELIB_RECORD_0_ID,
                DevelopmentData.ELIB_FORMAT_0_ID);
        String palmaUrl = ehubConsumer.getProperties().get(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL);
        endpoint = Endpoint.publish(palmaUrl + "/loans", new LoansImpl());
    }

    @After
    public void tearDown() {
        endpoint.stop();
    }

    @Test
    public void testCheckOutTest() {
        CheckoutTestAnalysis preCheckoutAnalysis =
                palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(preCheckoutAnalysis);
        assertEquals(CheckoutTestAnalysis.Result.ACTIVE_LOAN, preCheckoutAnalysis.getResult());
        assertEquals(DevelopmentData.LMS_LOAN_ID_1, preCheckoutAnalysis.getLmsLoanId());
    }

    @Test
    public void testCheckOut() {
        Date expirationDate = new Date();
        LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, expirationDate, DevelopmentData.ELIB_LIBRARY_CARD,
                DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(lmsLoan);
        assertEquals(DevelopmentData.LMS_LOAN_ID_1, lmsLoan.getId());
    }

    @WebService(serviceName = "LoansPalmaService", portName = "loans", targetNamespace = "http://loans.palma.services.arena.axiell.com/",
            wsdlLocation = "com/axiell/arena/palma/loans.wsdl", endpointInterface = "com.axiell.arena.services.palma.loans.Loans")
    public static class LoansImpl implements Loans {

        public com.axiell.arena.services.palma.loans.CheckOutTestResponse checkOutTest(CheckOutTest parameters) {
            try {
                JAXBContext jc = JAXBContext.newInstance("com.axiell.arena.services.palma.loans");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                @SuppressWarnings("unchecked")
                JAXBElement<com.axiell.arena.services.palma.loans.CheckOutTestResponse> jaxbElement =
                        (JAXBElement<com.axiell.arena.services.palma.loans.CheckOutTestResponse>) unmarshaller.unmarshal(
                                new ClassPathResource("com/axiell/arena/palma/CheckOutTestResponse.xml").getFile());
                return jaxbElement.getValue();
            } catch (JAXBException | IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        public com.axiell.arena.services.palma.loans.CheckOutResponse checkOut(CheckOut parameters) {
            try {
                JAXBContext jc = JAXBContext.newInstance("com.axiell.arena.services.palma.loans");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                @SuppressWarnings("unchecked")
                JAXBElement<com.axiell.arena.services.palma.loans.CheckOutResponse> jaxbElement =
                        (JAXBElement<com.axiell.arena.services.palma.loans.CheckOutResponse>) unmarshaller.unmarshal(
                                new ClassPathResource("com/axiell/arena/palma/CheckOutResponse.xml").getFile());
                return jaxbElement.getValue();
            } catch (JAXBException | IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        @Override
        public com.axiell.arena.services.palma.loans.RenewLoansResponse renewLoans(final RenewLoans parameters) {
            return null;
        }

        @Override
        public com.axiell.arena.services.palma.loans.GetLoansResponse getLoans(GetLoans parameters) {
            return null;
        }
    }
}
