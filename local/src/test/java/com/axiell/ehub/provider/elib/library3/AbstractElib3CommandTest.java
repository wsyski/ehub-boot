package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AssertCommand;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractElib3CommandTest {
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
}
