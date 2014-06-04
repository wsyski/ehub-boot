package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentFactory;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.internal.matchers.Matches;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CreateLoanCommandChainTest {
    private static final Date EXPIRATION_DATE = new Date();
    private static final String CONTENT_URL = "url";

    private CreateLoanCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private IContentFactory contentFactory;
    @Mock
    private CommandData commandData;
    @Mock
    private PendingLoan pendingLoan;
    @Mock
    private BookAvailability bookAvailability;
    @Mock
    private CreatedLoan createdLoan;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private ContentProviderLoan loan;
    @Mock
    private DownloadableContent downloadableContent;

    private ContentProviderLoan actualLoan;

    @Before
    public void setUp() {
        underTest = new CreateLoanCommandChain(elibFacade, exceptionFactory, contentFactory);
    }

    @Before
    public void setUpContentProviderLoanWithDefaultData() {
        given(loan.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(downloadableContent.getUrl()).willReturn(CONTENT_URL);
        given(loan.getContent()).willReturn(downloadableContent);
    }

    @Test
    public void execute() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(pendingLoan.getContentProviderRecordId()).willReturn("recordId");
        commandData = CommandData.newInstance(contentProviderConsumer, "libraryCard").setPendingLoan(pendingLoan);


        given(bookAvailability.isProductAvailable(any(String.class))).willReturn(true);
        given(elibFacade.getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(String.class))).willReturn(bookAvailability);

        given(createdLoan.getFirstContentUrl()).willReturn(CONTENT_URL);
        given(createdLoan.getExpirationDate()).willReturn(EXPIRATION_DATE);

        given(elibFacade.createLoan(any(ContentProviderConsumer.class), any(String.class), any(String.class))).willReturn(createdLoan);

        actualLoan = underTest.execute(commandData);
        assertThat(actualLoan.getContent(), is(loan.getContent()));
    }
}
