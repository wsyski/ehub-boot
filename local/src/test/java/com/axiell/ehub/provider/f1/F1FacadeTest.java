package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import se.forlagett.api.F1ServiceSoap;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class F1FacadeTest {
    private static final String FORMAT_ID = "1";
    private static final String LOAN_ID = "loanId";
    private static final String CONTENT = "content";
    public static final String CARD = "card";
    private F1Facade underTest;
    @Mock
    private CommandData commandData;
    @Mock
    private IF1ServiceSoapFactory f1ServiceSoapFactory;
    @Mock
    private F1ServiceSoap f1ServiceSoap;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private IF1SoapServiceParameterHelper f1SoapServiceParameterHelper;
    @Mock
    private ContentProviderLoanMetadata loanMetadata;
    @Mock
    private Patron patron;
    private GetFormatResponse actualGetFormatResponse;
    private CreateLoanResponse actualCreateLoanResponse;
    private GetLoanContentResponse actualGetLoanContentResponse;

    @Before
    public void setUpUnderTest() {
        underTest = new F1Facade();
        given(commandData.getContentProviderConsumer()).willReturn(contentProviderConsumer);
        given(patron.getLibraryCard()).willReturn(CARD);
        given(commandData.getPatron()).willReturn(patron);
        given(f1ServiceSoapFactory.getInstance(contentProviderConsumer)).willReturn(f1ServiceSoap);
        ReflectionTestUtils.setField(underTest, "f1ServiceSoapFactory", f1ServiceSoapFactory);
        ReflectionTestUtils.setField(underTest, "f1SoapServiceParameterHelper", f1SoapServiceParameterHelper);
    }

    @Test
    public void getFormats() {
        givenFormatIdFromSoapService();
        whenGetFormats();
        thenActualFormatIdEqualsExpectedFormatId();
        thenGetContentProviderConsumerFromCommandDataIsInvoked();
        thenGetContentProviderRecordIdFromCommandDataIsInvoked();
        thenGetLanguageFromCommandDataIsInvoked();
    }

    @Test
    public void createLoan() {
        givenF1ContentProviderConsumerProperties();
        givenLoanIdFromSoapService();
        whenCreateLoan();
        thenActualLoanIdEqualsExpectedLoanId();
        thenGetContentProviderConsumerFromCommandDataIsInvoked();
        thenGetContentProviderRecordIdFromCommandDataIsInvoked();
        thenGetLanguageFromCommandDataIsInvoked();
        thenGetPatronInCommandDataIsInvoked();
    }

    @Test
    public void getLoanContent() {
        givenContentProviderLoanMetadata();
        givenF1ContentProviderConsumerProperties();
        givenLoanContentFromSoapService();
        whenGetLoanContent();
        thenActualContentEqualsExpectedContent();
        thenGetContentProviderConsumerFromCommandDataIsInvoked();
        thenGetRecordIdFromContentProviderLoanMetadataIsInvoked();
        thenGetLanguageFromCommandDataIsInvoked();
        thenGetPatronInCommandDataIsInvoked();
        thenGetContentProviderFormatIdFromCommanDataIsInvoked();
    }

    private void thenGetRecordIdFromContentProviderLoanMetadataIsInvoked() {
        verify(loanMetadata, times(1)).getRecordId();
    }

    private void givenContentProviderLoanMetadata() {
        given(commandData.getContentProviderLoanMetadata()).willReturn(loanMetadata);
    }

    private void thenGetContentProviderConsumerFromCommandDataIsInvoked() {
        verify(commandData, times(1)).getContentProviderConsumer();
    }

    private void thenGetContentProviderRecordIdFromCommandDataIsInvoked() {
        verify(commandData, times(1)).getContentProviderRecordId();
    }

    private void thenGetLanguageFromCommandDataIsInvoked() {
        verify(commandData, times(1)).getLanguage();
    }

    private void thenGetContentProviderFormatIdFromCommanDataIsInvoked() {
        verify(commandData, times(1)).getContentProviderFormatId();
    }

    private void thenGetPatronInCommandDataIsInvoked() {
        verify(commandData, times(1)).getPatron();
    }

    private void thenActualContentEqualsExpectedContent() {
        assertEquals(CONTENT, actualGetLoanContentResponse.getValue());
    }

    private void givenLoanContentFromSoapService() {
        given(f1ServiceSoap.getLoanContent(anyInt(), anyString(), anyString(), anyInt(), anyString(), anyInt(), anyString(), anyInt())).willReturn(CONTENT);
    }

    private void whenGetLoanContent() {
        actualGetLoanContentResponse = underTest.getLoanContent(commandData);
    }

    private void thenActualLoanIdEqualsExpectedLoanId() {
        assertEquals(LOAN_ID, actualCreateLoanResponse.getValue());
    }

    private void givenF1ContentProviderConsumerProperties() {
        given(contentProviderConsumer.getProperty(F1_USERNAME)).willReturn("usr");
        given(contentProviderConsumer.getProperty(F1_PASSWORD)).willReturn("pwd");
        given(contentProviderConsumer.getProperty(F1_REGION_ID)).willReturn("1");
    }

    private void givenLoanIdFromSoapService() {
        given(f1ServiceSoap.createLoan(anyInt(), anyString(), anyString(), anyString(), anyInt(), anyString())).willReturn(LOAN_ID);
    }

    private void whenCreateLoan() {
        actualCreateLoanResponse = underTest.createLoan(commandData);
    }

    private void givenFormatIdFromSoapService() {
        given(f1ServiceSoap.getFormats(anyInt())).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
        actualGetFormatResponse = underTest.getFormats(commandData);
    }

    private void thenActualFormatIdEqualsExpectedFormatId() {
        assertEquals(FORMAT_ID, actualGetFormatResponse.getValue());
    }
}
