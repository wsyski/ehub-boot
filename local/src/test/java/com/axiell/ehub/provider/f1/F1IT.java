package com.axiell.ehub.provider.f1;

import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.F1_PASSWORD;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.F1_REGION_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.F1_USERNAME;
import static junit.framework.Assert.*;
import static org.mockito.BDDMockito.given;

public class F1IT extends AbstractContentProviderIT {
    private static final String API_BASE_URL_VALUE = "http://194.68.86.40/F1Service.asmx?WSDL";
    private static final String F1_USERNAME_VALUE = "Testet";
    private static final String F1_PASSWORD_VALUE = "nF7yGX";
    private static final String F1_REGION_ID_VALUE = "87";
    private static final String CARD = "78654387";
    //    private static final String CP_RECORD_ID = "1084";
    private static final String LANGUAGE = "en";

//    private static final String CP_LOAN_ID = "48161";
//    private static final String EXPECTED_VALID_TYPE_ID = "3";
//    private static final String CP_RECORD_ID = "519";

    private static final String CP_RECORD_ID = "66";
    private static final String CP_LOAN_ID = "49300";
    private static final String EXPECTED_VALID_TYPE_ID = "1";

    private F1Facade underTest;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private ContentProviderLoanMetadata loanMetadata;
    @Mock
    private Patron patron;
    @Mock
    private CommandData data;
    private GetFormatResponse actualGetFormatResponse;
    private CreateLoanResponse actualCreateLoanResponse;
    private GetLoanContentResponse actualGetLoanContentResponse;

    @Before
    public void setUpUnderTest() {
        final IF1ServiceSoapFactory f1ServiceSoapFactory = makeF1SServiceSoapFactory();
        final IF1SoapServiceParameterHelper f1SoapServiceParameterHelper = new F1SoapServiceParameterHelper();

        underTest = new F1Facade();
        ReflectionTestUtils.setField(underTest, "f1ServiceSoapFactory", f1ServiceSoapFactory);
        ReflectionTestUtils.setField(underTest, "f1SoapServiceParameterHelper", f1SoapServiceParameterHelper);
    }

    @Test
    public void getFormats_validRecordId() {
        givenContentProvider();
        givenApiBaseUrl();
        givenContentProviderConsumerInCommandData();
        givenValidContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        whenGetFormats();
        thenActualTypeIdEqualsExpectedTypeId();
    }

    @Test
    public void getFormats_invalidRecordId() {
        givenContentProvider();
        givenApiBaseUrl();
        givenContentProviderConsumerInCommandData();
        givenInvalidContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        whenGetFormats();
        thenActualFormatEqualsNoSuchFormat();
    }

    @Test
    public void createLoan() {
        givenContentProvider();
        givenApiBaseUrl();
        givenF1Credentials();
        givenContentProviderConsumerInCommandData();
        givenValidContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenLibraryCardInCommandData();
        whenCreateLoan();
        thenActualCreateLoanContainsValidLoanReference();
    }

    @Test
    public void getLoanContent() {
        givenContentProvider();
        givenApiBaseUrl();
        givenF1Credentials();
        givenContentProviderConsumerInCommandData();
        givenLanguageInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenLibraryCardInCommandData();
        givenValidContentProviderRecordIdInCommandData();
        whenGetLoanContent();
        thenActualLoanContentIsValid();
    }

    private void givenContentProviderLoanMetadataInCommandData() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(EXPECTED_VALID_TYPE_ID);
        given(loanMetadata.getFormatDecoration()).willReturn(formatDecoration);
        given(loanMetadata.getRecordId()).willReturn(CP_RECORD_ID);
        given(loanMetadata.getId()).willReturn(CP_LOAN_ID);
        given(data.getContentProviderLoanMetadata()).willReturn(loanMetadata);
    }

    private void thenActualLoanContentIsValid() {
        assertTrue(actualGetLoanContentResponse.isValidContent());
    }

    private void whenGetLoanContent() {
        actualGetLoanContentResponse = underTest.getLoanContent(data);
    }

//    private void givenValidFormatIdInCommandData() {
//        given(data.getContentProviderFormatId()).willReturn(EXPECTED_VALID_TYPE_ID);
//    }

    private void thenActualCreateLoanContainsValidLoanReference() {
        System.out.println(actualCreateLoanResponse.getValue());
        assertTrue(actualCreateLoanResponse.isValidLoan());
    }

    private void whenCreateLoan() {
        actualCreateLoanResponse = underTest.createLoan(data);
    }

    private void givenLibraryCardInCommandData() {
        given(patron.getLibraryCard()).willReturn(CARD);
        given(data.getPatron()).willReturn(patron);

    }

    private void thenActualFormatEqualsNoSuchFormat() {
        assertEquals(GetFormatResponse.NO_SUCH_FORMAT, actualGetFormatResponse.getValue());
        assertFalse(actualGetFormatResponse.isValidFormat());
    }

    private void givenLanguageInCommandData() {
        given(data.getLanguage()).willReturn(LANGUAGE);
    }

    private void givenInvalidContentProviderRecordIdInCommandData() {
        given(data.getContentProviderRecordId()).willReturn("0");
    }

    private void givenValidContentProviderRecordIdInCommandData() {
        given(data.getContentProviderRecordId()).willReturn(CP_RECORD_ID);
    }

    private void givenContentProviderConsumerInCommandData() {
        given(data.getContentProviderConsumer()).willReturn(contentProviderConsumer);
    }

    private IF1ServiceSoapFactory makeF1SServiceSoapFactory() {
        IF1ServiceSoapFactory f1ServiceSoapFactory = new F1ServiceSoap12Factory();
        ReflectionTestUtils.setField(f1ServiceSoapFactory, "f1ServiceFactory", new F1ServiceFactory());
        return f1ServiceSoapFactory;
    }

    private void thenActualTypeIdEqualsExpectedTypeId() {
        assertEquals(EXPECTED_VALID_TYPE_ID, actualGetFormatResponse.getValue());
        assertTrue(actualGetFormatResponse.isValidFormat());
    }

    private void whenGetFormats() {
        actualGetFormatResponse = underTest.getFormats(data);
    }

    private void givenApiBaseUrl() {
        given(contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL)).willReturn(API_BASE_URL_VALUE);
    }

    private void givenF1Credentials() {
        given(contentProviderConsumer.getProperty(F1_USERNAME)).willReturn(F1_USERNAME_VALUE);
        given(contentProviderConsumer.getProperty(F1_PASSWORD)).willReturn(F1_PASSWORD_VALUE);
        given(contentProviderConsumer.getProperty(F1_REGION_ID)).willReturn(F1_REGION_ID_VALUE);
    }
}
