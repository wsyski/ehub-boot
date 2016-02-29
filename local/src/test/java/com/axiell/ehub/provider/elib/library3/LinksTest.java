package com.axiell.ehub.provider.elib.library3;

import junit.framework.Assert;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class LinksTest {
    private static final String FORMAT_ID = "4103";
    private static final String EXP_URL = "https://webservices.elib.se/v2/Fulfillment/?data=...omitted";
    private static final String CREATED_LOAN_JSON =
            "{ \"CreatedLoan\": { \"ProductID\": 1002445, \"LoanID\": 100000094, \"ExpiryDate\": \"2014-04-18\", \"Links\": [{ \"Contents\": [{ \"Part\": 1, \"Url\": \"https://webservices.elib.se/v2/Fulfillment/?data=...omitted\" }], \"FormatID\": 4103 }], \"CreatedDate\": \"2014-03-21\", \"Supplements\": [], \"CoverImage\": \"http://www.elib.se/cover_images/9185201227.jpg\", \"Active\": true, \"Title\": \"Ringaren i Notre-Dame\" } }";
    private static final String LOAN_JSON =
            "{ \"Loan\": { \"ProductID\": 1002445, \"LoanID\": 100000094, \"ExpiryDate\": \"2014-04-18\", \"Links\": [{ \"Contents\": [{ \"Part\": 1, \"Url\": \"https://webservices.elib.se/v2/Fulfillment/?data=...omitted\" }], \"FormatID\": 4103 }], \"CreatedDate\": \"2014-03-21\", \"Supplements\": [], \"CoverImage\": \"http://www.elib.se/cover_images/9185201227.jpg\", \"Active\": true, \"Title\": \"Ringaren i Notre-Dame\" } }";
    private ObjectMapper mapper;
    private List<String> contentUrls;

    @Before
    public void setUpObjectMapper() {
        mapper = new ObjectMapper();
    }

    @Test
    public void createdLoan() throws IOException {
        whenGetContentFromCreatedLoan();
        thenActualUrlEqualsExpectedUrl();
    }

    @Test
    public void loan() throws IOException {
        whenGetContentFromLoan();
        thenActualUrlEqualsExpectedUrl();
    }

    private void whenGetContentFromCreatedLoan() throws IOException {
        StringReader reader = new StringReader(CREATED_LOAN_JSON);
        CreateLoanResponse createLoanResponse = mapper.readValue(reader, CreateLoanResponse.class);
        CreatedLoan createdLoan = createLoanResponse.getCreatedLoan();
        contentUrls = createdLoan.getContentUrlsFor(FORMAT_ID);
    }

    private void whenGetContentFromLoan() throws IOException {
        StringReader reader = new StringReader(LOAN_JSON);
        GetLoanResponse getLoanResponse = mapper.readValue(reader, GetLoanResponse.class);
        Loan loan = getLoanResponse.getLoan();
        contentUrls = loan.getContentUrlsFor(FORMAT_ID);
    }

    private void thenActualUrlEqualsExpectedUrl() {
        Assert.assertEquals(EXP_URL, contentUrls.get(0));
    }
}
