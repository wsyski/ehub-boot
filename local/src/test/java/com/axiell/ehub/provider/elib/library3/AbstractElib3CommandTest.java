package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AssertCommand;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractElib3CommandTest {
    protected static final String PRODUCT_ID = "id";
    protected static final String LANGUAGE = "en";
    @Mock
    protected IElibFacade elibFacade;
    @Mock
    protected IEhubExceptionFactory exceptionFactory;
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    protected String libraryCard;
    protected String elibProductId;
    @Mock
    protected PendingLoan pendingLoan;
    @Mock
    protected InternalServerErrorException internalServerErrorException;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private FormatDecoration formatDecoration;
    protected ErrorCauseArgumentValue.Type argValueType;
    protected String language;
    protected AssertCommand next;
    protected CommandData data;

    @Before
    public void setUpAssertCommand() {
        next = new AssertCommand();
    }

    protected void givenBasicCommandData() {
        data = CommandData.newInstance(contentProviderConsumer, libraryCard).setPendingLoan(pendingLoan).setLanguage(language);
    }

    protected void thenCommandIsInvoked() {
        next.verifyInvocation();
    }

    protected void thenInternalServerErrorExceptionShouldHaveBeenThrown() {
        fail("An InternalServerErrorException should have been thrown");
    }

    protected void givenContentProviderFromContentProviderConsumer() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    protected void givenFormatDecorationForFormatId() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }
}
