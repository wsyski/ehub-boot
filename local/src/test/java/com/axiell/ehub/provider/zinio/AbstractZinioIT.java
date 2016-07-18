package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.error.EhubExceptionFactoryStub;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractZinioIT extends AbstractContentProviderIT {
    private static final long EHUB_CONSUMER_ID = 1L;
    private static final String EMAIL = "wos@axiell.com";
    private static final String API_BASE_URL = "http://www.rbdigitaltest.com";
    private static final String ZINIO_LIB_ID = "axielltest";
    private static final String ZINIO_TOKEN = "B5R7OfqTJaoeVeO0";
    private static final String LIBRARY_CARD = "12345";
    protected static final String RECORD_ID = "RBZ0000111";
    protected static final String INVALID_RECORD_ID = "invalidRecordId";
    protected static final String ISSUE_ID = "416342560";

    protected ZinioFacade underTest;

    @Mock
    protected Patron patron;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        underTest = new ZinioFacade();
        IEhubExceptionFactory ehubExceptionFactory = new EhubExceptionFactoryStub();
        IZinioResponseFactory zinioResponseFactory = new ZinioResponseFactory();
        ReflectionTestUtils.setField(zinioResponseFactory, "ehubExceptionFactory", ehubExceptionFactory);
        ReflectionTestUtils.setField(underTest, "zinioResponseFactory", zinioResponseFactory);
    }

    protected void givenConfigurationProperties() {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(ContentProvider.CONTENT_PROVIDER_ZINIO);
        given(contentProvider.isLoanPerProduct()).willReturn(false);
        given(contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL)).willReturn(API_BASE_URL);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID)).willReturn(ZINIO_LIB_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN)).willReturn(ZINIO_TOKEN);
    }

    protected void givenPatron() {
        given(patron.hasEmail()).willReturn(true);
        given(patron.getEmail()).willReturn(EMAIL);
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(LIBRARY_CARD);
    }

    protected void givenExpectedInternalServerException(final String code) {
        expectedException.expect(InternalServerErrorException.class);
        expectedException.expect(new ContentProviderErrorExceptionMatcher(InternalServerErrorException.class, ContentProvider.CONTENT_PROVIDER_ZINIO, code));
    }
}
