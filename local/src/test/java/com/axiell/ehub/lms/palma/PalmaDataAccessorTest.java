package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.*;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.Endpoint;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/axiell/ehub/common-context.xml")
public class PalmaDataAccessorTest {
    protected static Endpoint ENDPOINT;
    protected static String ENDPOINT_ADDRESS;

    @Autowired
    private IPalmaDataAccessor palmaDataAccessor;

    private EhubConsumer ehubConsumer;
    private PendingLoan pendingLoan;

    @BeforeClass
    public static void beforeClass() {
        ENDPOINT_ADDRESS = "http://localhost:16521/arena.pa.palma/loans";
        ENDPOINT = Endpoint.publish(ENDPOINT_ADDRESS, new LoansImpl());
    }

    @AfterClass
    public static void afterClass() {
        ENDPOINT.stop();
    }

    @Before
    public void setUp() throws Exception {
        palmaDataAccessor = new PalmaDataAccessor();
        ehubConsumer = createEhubConsumer();
        ReflectionTestUtils.setField(ehubConsumer, "id", 1L);
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), DevelopmentData.ELIB_RECORD_0_ID,
                DevelopmentData.ELIB_FORMAT_0_ID);
        URL arenaPalmaURL = (new File("src/main/resources/com/axiell/arena/palma")).toURI().toURL();
        ehubConsumer.getProperties().put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, arenaPalmaURL.toString());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCheckOutTest() {
        PreCheckoutAnalysis preCheckoutAnalysis =
                palmaDataAccessor.preCheckout(ehubConsumer, pendingLoan, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(preCheckoutAnalysis);
        assertEquals(PreCheckoutAnalysis.Result.ACTIVE_LOAN, preCheckoutAnalysis.getResult());
        assertEquals(DevelopmentData.LMS_LOAN_ID, preCheckoutAnalysis.getLmsLoanId());
    }

    @Test
    public void testCheckOut() {
        Date expirationDate = new Date();
        LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, expirationDate, DevelopmentData.ELIB_LIBRARY_CARD,
                DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(lmsLoan);
        assertEquals(DevelopmentData.LMS_LOAN_ID, lmsLoan.getId());
    }


    public EhubConsumer createEhubConsumer() {
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, DevelopmentData.ARENA_PALMA_URL);
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        EhubConsumer ehubConsumer = new EhubConsumer("Ehub Consumer Description", DevelopmentData.EHUB_CONSUMER_SECRET_KEY, ehubConsumerProperties);
        return ehubConsumer;
    }

    @WebService(
            serviceName = "LoansPalmaService",
            portName = "loans",
            targetNamespace = "http://loans.palma.services.arena.axiell.com/",
            wsdlLocation = "com/axiell/arena/palma/loans.wsdl",
            endpointInterface = "com.axiell.arena.services.palma.loans.Loans")

    public static class LoansImpl implements Loans {

        public com.axiell.arena.services.palma.loans.CheckOutTestResponse checkOutTest(CheckOutTest parameters) {
            try {
                JAXBContext jc = JAXBContext.newInstance("com.axiell.arena.services.palma.loans");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
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
