package com.axiell.ehub.it.remote;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.error.EhubExceptionMatcher;
import com.axiell.ehub.error.LmsErrorExceptionMatcher;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.test.controller.internal.dto.TestDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public abstract class RemoteITFixture extends WireMockITFixture {
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();

    protected TestDataDTO testData;
    protected AuthInfo authInfo;

    @BeforeEach
    public void setUp() throws EhubException {
        initTestData();
        initAuthInfo();
    }

    @AfterEach
    public void tearDown() {
        deleteTestData();
        resetWireMock();
    }


    private void initTestData() {
        testData = getTestDataServiceClient().init(getContentProviderName(), isLoanPerProduct());
    }

    private void deleteTestData() {
        getTestDataServiceClient().delete();
    }


    private void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder().ehubConsumerId(testData.getEhubConsumerId()).patron(getPatron()).build();
    }

    protected void thenExpectedContentProviderErrorException(final Exception exception, final String status) {
        assertThat(exception, Matchers.is(new ContentProviderErrorExceptionMatcher(EhubException.class, getContentProviderName(), status)));
    }


    protected void thenExpectedLmsErrorException(final Exception exception, final String status) {
        assertThat(exception, Matchers.is(new LmsErrorExceptionMatcher(EhubException.class, testData.getEhubConsumerId(), status)));
    }

    protected void thenExpectedEhubException(final Exception exception, final Class clazz, final ErrorCause errorCause, final ErrorCauseArgument... arguments) {
        assertThat(exception, Matchers.is(new EhubExceptionMatcher(EhubException.class, errorCause, arguments)));
    }

    protected String getContentProviderAlias() {
        return TestDataConstants.CONTENT_PROVIDER_ALIAS_PREFIX + getContentProviderName();
    }


    protected Patron getPatron() {
        Patron.Builder patronBuilder = new Patron.Builder().libraryCard(testData.getLibraryCard()).pin(testData.getPin()).id(testData.getPatronId()).email(testData.getEmail()).name(testData.getName()).birthDate(testData.getBirthDate());
        return patronBuilder.build();
    }

    protected abstract IEhubServiceClient getEhubServiceClient();

    protected abstract ITestDataServiceClient getTestDataServiceClient();
}
