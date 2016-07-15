package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.error.EhubExceptionFactoryStub;
import com.axiell.ehub.error.IEhubExceptionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public abstract class ContentProviderExceptionFactoryTestFixture<E> {
    protected static final String LANGUAGE = "en";

    @Mock
    protected Response response;

    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;

    protected IEhubExceptionFactory ehubExceptionFactory = new EhubExceptionFactoryStub();

    protected InternalServerErrorException internalServerErrorException;

    protected IContentProviderExceptionFactory<E> underTest;

    @Before
    public void setUp() {
        givenContentProviderConsumer();
        givenContentProvider();
        givenResponse();
    }

    @Test
    public void nullErrorEntity() {
        whenCreateExecuted();
        thenInternalServerErrorWithOnlyMessage(AbstractContentProviderExceptionFactory.DEFAULT_MESSAGE);
    }

    protected void thenInternalServerErrorWithOnlyMessage(final String message) {
        List<ErrorCauseArgument> errorCauseArguments = internalServerErrorException.getEhubError().getArguments();
        assertThat(errorCauseArguments.size(), is(2));
        thenValidContentProviderName(errorCauseArguments.get(0));
        assertThat(errorCauseArguments.get(1).getType(), is(ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS));
        thenInternalServerErrorExceptionHasMessage(message);
    }

    protected void thenExpectedContentProviderErrorException(final String code) {
        assertThat(internalServerErrorException,is(new ContentProviderErrorExceptionMatcher(InternalServerErrorException.class, getContentProviderName(),
                code)));
    }

    private void thenValidContentProviderName(final ErrorCauseArgument errorCauseArgument) {
        assertThat(errorCauseArgument.getValue(), is(getContentProviderName()));
    }

    protected void thenInternalServerErrorExceptionHasMessage(final String message) {
        assertThat(internalServerErrorException.getMessage(), is(message));
    }


    protected abstract void whenCreateExecuted();

    protected abstract String getContentProviderName();

    private void givenContentProviderConsumer() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    private void givenContentProvider() {
        given(contentProvider.getName()).willReturn(getContentProviderName());
    }

    private void givenResponse() {
        given(response.getStatusInfo()).willReturn(Response.Status.INTERNAL_SERVER_ERROR);
    }
}
