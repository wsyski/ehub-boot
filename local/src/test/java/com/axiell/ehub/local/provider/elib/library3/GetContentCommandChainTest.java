package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.checkout.ContentLink;
import com.axiell.ehub.common.checkout.ContentLinkBuilder;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.provider.CommandData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetContentCommandChainTest {
    private static final String LOAN_ID = "loanId";
    private static final String FORMAT_ID = "formatId";

    private GetContentCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private ContentProviderLoanMetadata loanMetadata;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private LoanDTO loan;

    private CommandData data;
    private ContentLink actualContentLink;

    @BeforeEach
    public void setUpUnderTest() {
        underTest = new GetContentCommandChain(elibFacade, exceptionFactory);
    }

    @Test
    public void execute() {
        givenCommandData();
        givenActiveLoanWithContentUrl();
        whenExecute();
        thenActualContentEqualsExpectedContent();
    }

    private void givenActiveLoanWithContentUrl() {
        given(loan.isActive()).willReturn(true);
        given(loan.getContentUrlsFor(any(String.class))).willReturn(Collections.singletonList(ContentLinkBuilder.HREF));
        given(elibFacade.getLoan(any(ContentProviderConsumer.class), any(String.class))).willReturn(loan);
    }

    private void whenExecute() {
        actualContentLink = underTest.execute(data).getContentLinks().getContentLinks().get(0);
    }

    private void thenActualContentEqualsExpectedContent() {
        Assertions.assertEquals(ContentLinkBuilder.HREF, actualContentLink.href());
    }

    private void givenCommandData() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(loanMetadata.getId()).willReturn(LOAN_ID);
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
        data = CommandData.newInstance(contentProviderConsumer, new Patron.Builder().libraryCard("card").pin("pin").build(), "sv").setContentProviderLoanMetadata(loanMetadata).setFormatDecoration(formatDecoration);
    }
}
