package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.record.format.Formats;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class Elib3DataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "recordId";

    private Elib3DataAccessor underTest;
    @Mock
    private IElib3CommandChainFactory commandChainFactory;
    @Mock
    private GetFormatsCommandChain getFormatsCommandChain;
    @Mock
    private Formats expectedFormats;
    @Mock
    private CreateLoanCommandChain createLoanCommandChain;
    @Mock
    private ContentProviderLoan expectedLoan;
    @Mock
    private GetContentCommandChain getContentCommandChain;
    @Mock
    private IContent expectedContent;

    @Before
    public void setUpUnderTest() {
        underTest = new Elib3DataAccessor();
        ReflectionTestUtils.setField(underTest, "commandChainFactory", commandChainFactory);
    }

    @Test
    public void getFormats() {
        givenExpectedFormats();
        givenGetFormatsCommandChain();
        whenGetFormats();
        thenActualFormatsEqualsExpectedFormats();
    }

    @Test
    public void createLoan() {
        givenExpectedLoan();
        givenCreateLoanCommandChain();
        whenCreateLoan();
        thenActualLoanEqualsExpectedLoan();
    }

    @Test
    public void getContent() {
        givenExpectedContent();
        givenGetContentCommandChain();
        whenGetContent();
        thenActualContentEqualsExpectedContent();
    }

    private void givenExpectedContent() {
        given(getContentCommandChain.execute(any(Elib3CommandData.class))).willReturn(expectedContent);
    }

    private void givenGetContentCommandChain() {
        given(commandChainFactory.createGetContentCommandChain()).willReturn(getContentCommandChain);
    }

    private void whenGetContent() {
        actualContent = underTest.getContent(contentProviderConsumer, CARD, PIN, loanMetadata, LANGUAGE);
    }

    private void thenActualContentEqualsExpectedContent() {
        assertThat(actualContent, is(expectedContent));
    }

    private void givenExpectedLoan() {
        given(createLoanCommandChain.execute(any(Elib3CommandData.class))).willReturn(expectedLoan);
    }

    private void givenCreateLoanCommandChain() {
        given(commandChainFactory.createCreateLoanCommandChain()).willReturn(createLoanCommandChain);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(contentProviderConsumer, CARD, PIN, pendingLoan, LANGUAGE);
    }

    private void thenActualLoanEqualsExpectedLoan() {
        assertThat(actualLoan, is(expectedLoan));
    }

    private void givenExpectedFormats() {
        given(getFormatsCommandChain.execute(any(Elib3CommandData.class))).willReturn(expectedFormats);
    }

    private void givenGetFormatsCommandChain() {
        given(commandChainFactory.createGetFormatsCommandChain()).willReturn(getFormatsCommandChain);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(contentProviderConsumer, CARD, RECORD_ID, LANGUAGE);
    }

    private void thenActualFormatsEqualsExpectedFormats() {
        assertThat(actualFormats, is(expectedFormats));
    }
}
