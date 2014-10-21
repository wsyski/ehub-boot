package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AssertCommand;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static com.axiell.ehub.provider.ContentProviderName.ELIB3;
import static junit.framework.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractElib3CommandTest {
    protected static final String PRODUCT_ID = "id";
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    @Mock
    protected IElibFacade elibFacade;
    @Mock
    protected IEhubExceptionFactory exceptionFactory;
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    protected String elibProductId;
    @Mock
    protected PendingLoan pendingLoan;
    @Mock
    protected InternalServerErrorException internalServerErrorException;
    @Mock
    protected ContentProvider contentProvider;
    @Mock
    protected FormatDecoration formatDecoration;
    @Mock
    protected Patron patron;

    @Mock
    private EhubConsumer ehubConsumer;

    protected ErrorCauseArgumentValue.Type argValueType;
    protected String language;
    protected AssertCommand next;
    protected CommandData data;

    @Before
    public void setUpAssertCommand() {
        next = new AssertCommand();
    }

    @Before
    public void setUpContentProviderConsumer() {
       givenContentProviderConsumer();
    }

    protected void givenBasicCommandData() {
        data = CommandData.newInstance(contentProviderConsumer, patron).setPendingLoan(pendingLoan).setLanguage(language);
    }

    private void givenContentProviderConsumer() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(ELIB3);
        given(ehubConsumer.getDefaultLanguage()).willReturn(new Language(LANGUAGE));
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
