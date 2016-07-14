package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.ContentBuilder;
import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class Elib3DataAccessorTest extends ContentProviderDataAccessorTestFixture<Elib3DataAccessor> {

    @Mock
    private IElib3CommandChainFactory commandChainFactory;
    @Mock
    private GetFormatsCommandChain getFormatsCommandChain;
    @Mock
    private CreateLoanCommandChain createLoanCommandChain;
    @Mock
    private ContentProviderLoan expectedLoan;
    @Mock
    private GetContentCommandChain getContentCommandChain;

    private List<Format> expectedFormats = Collections.singletonList(FormatBuilder.streamingFormat());

    @Before
    public void setUpUnderTest() {
        underTest = new Elib3DataAccessor();
        ReflectionTestUtils.setField(underTest, "commandChainFactory", commandChainFactory);
    }

    @Test
    public void getFormats() {
        givenExpectedFormats();
        givenGetFormatsCommandChain();
        whenGetIssues();
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
        given(getContentCommandChain.execute(any(Elib3CommandData.class))).willReturn(ContentBuilder.contentWithSupplementLinks());
    }

    private void givenGetContentCommandChain() {
        given(commandChainFactory.createGetContentCommandChain()).willReturn(getContentCommandChain);
    }

    private void whenGetContent() {
        actualContentLink = underTest.getContent(commandData).getContentLinks().getContentLinks().get(0);
    }

    private void thenActualContentEqualsExpectedContent() {
        assertThat(actualContentLink.href(), is(ContentLinkBuilder.HREF));
    }

    private void givenExpectedLoan() {
        given(createLoanCommandChain.execute(any(Elib3CommandData.class))).willReturn(expectedLoan);
    }

    private void givenCreateLoanCommandChain() {
        given(commandChainFactory.createCreateLoanCommandChain()).willReturn(createLoanCommandChain);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
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

    private void thenActualFormatsEqualsExpectedFormats() {
        assertThat(getActualFormats(), is(expectedFormats));
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ELIB3;
    }
}
