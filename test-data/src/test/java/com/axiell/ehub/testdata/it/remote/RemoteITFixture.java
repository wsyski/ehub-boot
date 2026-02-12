package com.axiell.ehub.testdata.it.remote;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.EhubWebApplicationException;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.client.IEhubServiceClient;
import com.axiell.ehub.common.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.common.error.EhubExceptionMatcher;
import com.axiell.ehub.common.error.LmsErrorExceptionMatcher;
import com.axiell.ehub.testdata.ITestDataServiceClient;
import com.axiell.ehub.testdata.TestDataConstants;
import com.axiell.ehub.testdata.controller.internal.dto.TestDataDTO;
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
    public void setUp() throws EhubWebApplicationException {
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


    private void initAuthInfo() throws EhubWebApplicationException {
        authInfo = new AuthInfo.Builder().ehubConsumerId(testData.getEhubConsumerId()).patron(getPatron()).build();
    }

    protected void thenExpectedContentProviderErrorException(final Exception exception, final String status) {
        assertThat(exception, Matchers.is(new ContentProviderErrorExceptionMatcher(EhubWebApplicationException.class, getContentProviderName(), status)));
    }


    protected void thenExpectedLmsErrorException(final Exception exception, final String status) {
        assertThat(exception, Matchers.is(new LmsErrorExceptionMatcher(EhubWebApplicationException.class, testData.getEhubConsumerId(), status)));
    }

    protected void thenExpectedEhubException(final Exception exception, final Class clazz, final ErrorCause errorCause, final ErrorCauseArgument... arguments) {
        assertThat(exception, Matchers.is(new EhubExceptionMatcher(EhubWebApplicationException.class, errorCause, arguments)));
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
