package com.axiell.ehub.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AssertCommand;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    protected ErrorCauseArgumentType argValueType;
    protected String language;
    protected AssertCommand next;
    protected CommandData data;


    @BeforeEach
    public void setUpAssertCommand() {
        next = new AssertCommand();
    }

    @BeforeEach
    public void setUpContentProviderConsumer() {
        givenContentProviderConsumer();
    }

    protected void givenLanguage() {
        language = LANGUAGE;
    }

    protected void givenBasicCommandData() {
        given(pendingLoan.contentProviderRecordId()).willReturn(PRODUCT_ID);
        given(pendingLoan.contentProviderFormatId()).willReturn("contentProviderFormatId");
        data = CommandData.newInstance(contentProviderConsumer, patron, language).setPendingLoan(pendingLoan);
    }

    private void givenContentProviderConsumer() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(ContentProvider.CONTENT_PROVIDER_ELIB3);
        given(ehubConsumer.getDefaultLanguage()).willReturn(new Language(LANGUAGE));
    }

    protected void thenCommandIsInvoked() {
        next.verifyInvocation();
    }

    protected void thenInternalServerErrorExceptionShouldHaveBeenThrown() {
        Assertions.fail("An InternalServerErrorException should have been thrown");
    }

    protected void givenContentProviderFromContentProviderConsumer() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    protected void givenFormatDecorationForFormatId() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }
}
